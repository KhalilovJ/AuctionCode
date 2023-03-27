package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.DTOs.LotFrontDto;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisWaitingPayment;
import az.code.auctionbackend.repositories.auctionRepositories.BidRepo;
import az.code.auctionbackend.repositories.auctionRepositories.LotRepository;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.BidService;
import az.code.auctionbackend.services.interfaces.LotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private final AccountServiceImpl accountService;
    private final RedisRepository redisRepository;
    private final UserServiceImpl userService;
    private final BidRepo bidRepo;
    private final ApplicationContext ax;
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public Lot save(Lot lot) {
        return lotRepository.save(lot);
    }

    @Override
    public List<Lot> getAllLots() {
        return bidRepo.getAllLots();
    }

    @Override
    public Optional<Lot> findLotById(long id) {
        return Optional.of(bidRepo.getLotById(id));
    }

//    public Lot findRedisLotByIdActive(long id){
//
//        Lot lotMain = auctionRealtimeRepo.getLot(id);
//
//        if (lotMain == null){
//            lotMain = findLotById(id).orElse(null);
//            auctionRealtimeRepo.addLot(lotMain);
//        }
//        return lotMain;
//
//    }

    @Transactional
    public void createLot(LotDto lotDto, String images, String username){
        UserProfile user = userService.findByUsername(username).orElse(null);
        Lot lot = lotDto.getLot();
        lot.setItemPictures(images);
        lot.setUser(user);
        lot.setStatus(0);
        // save(lot) - writes data to the database
        Lot tmpLot = save(lot);

        System.out.println("LOT ID " + tmpLot.getId());


        RedisLot redisLot = objectMapper.convertValue(lotDto, RedisLot.class);
        redisLot.setId(lot.getId());
        redisLot.setItemPictures(images);
        redisLot.setUserId(user.getId());

//        List<BidDto> bidList = new ArrayList<>();
//        bidList.add(BidDto.builder().bid(5).bidTime(LocalDateTime.now()).lotId(lot.getId()).userId(user.getId()).build());
//
//        test.setBidHistory(bidList);

        redisRepository.saveRedis(redisLot);
        log.info("createLot DONE " + LocalDateTime.now());
//        auctionRealtimeRepo.addLot(tmpLot);
    }

    public Lot changeStatus(Long lotId, int status) {
        Lot lot = bidRepo.getLotById(lotId);
        log.warn(lot.toString());
        lot.setStatus(status);

        return lot;
    }

    @Transactional
    public void closeLot(long lotId) {
        log.info("IN closeLot v2");

        // 2 - auction finished
//        Lot lot = changeStatus(lotId, 2); // we need to change status according to closing process
        Lot lot = bidRepo.getLotById(lotId);
        log.warn(lot.toString());

        RedisLot redisLot = redisRepository.getRedis(lotId);
        List<BidDto> bidDtoList = redisLot.getBidHistory();

        List<Bid> bidList = new ArrayList<>();

        log.info("redisLot.getBidHistory() - done");
        log.info("bidList " + bidDtoList);

        // nobody made bids ==============================================
        if (bidDtoList == null) {
            log.info("nobody made bids");
            bidDtoList = new ArrayList<>();
            lot.setBidHistory(bidList);
            lot.setStatus(-2);
        }

        // somebody made bids =============================================
        if (!bidDtoList.isEmpty()) {
            log.info("somebody made bids");
            log.info("Bids: " + bidDtoList);

            for (BidDto bidDto : bidDtoList) {
                bidList.add(Bid.builder()
                        .lot(lot)
                        .bid(bidDto.getBid())
                        .user(userService.findProfileById(bidDto.getUserId()).get())
                        .bidTime(bidDto.getBidTime())
                        .build());
            }

            lot.setBidHistory(bidList);

            Bid winnerBid = getWinnerBid(lot);

            lot.setCurrentBid(winnerBid.getBid());


                int purchaseStatus;
                long winnerAccId = winnerBid.getUser().getAccount().getId();
                long sellerAccId = lot.getUser().getAccount().getId();
                double winnerBidAmount = winnerBid.getBid();
                purchaseStatus = accountService.purchase(
                        winnerAccId,
                        sellerAccId,
                        winnerBidAmount
                );

                log.info("Purchase status is: " + purchaseStatus);

                switch (purchaseStatus){
                    case -1:
                        lot.setStatus(-2);
                        userService.blockUser(winnerBid.getUser().getId(), true);
                        break;
                    case 0:
                            redisRepository.addWaitingPayment(
                                    RedisWaitingPayment.builder()
                                            .id(lot.getId())
                                            .creationTime(LocalDateTime.now())
                                            .receiverId(sellerAccId)
                                            .senderId(winnerAccId)
                                            .amount(winnerBidAmount)
                                            .build()
                            );
                        lot.setStatus(2);
                        userService.blockUser(winnerBid.getUser().getId(), true);
                        break;
                    case 1:
                        lot.setStatus(3);
                        break;}

                lot.setLotWinner(winnerBid.getUser());

        }
        //========================^ if someone made bids

        log.warn(lot.toString());
        bidRepo.saveLot(lot);

        // как то отправляем клиенту добрую весть :)
        System.out.println("WINNER!");
        // delete from Redis
        redisRepository.deleteRedis(lotId);
    }

    private Bid getWinnerBid(Lot lot){
        Bid bid = lot.getBidHistory().get(0);
        for (Bid bid1: lot.getBidHistory()){
            if (
                    bid1.getBid() > bid.getBid() ||
                    bid1.getBid() == bid.getBid() && bid.getBidTime().isAfter(bid1.getBidTime())
            ){
                bid = bid1;
            }
        }
        log.info("Lot " + lot.getId() + " winner: " + bid.getUser().getUsername() + " Bid " + bid.getBid());
        return bid;
    }

    /**
     * loads all lots with status 0 and 1 from the database into Redis memory if the redis is empty
     */
    @PostConstruct
    private void lotsImport(){

            lotRepository.getAllNonFinishedLots()
                    .forEach(l -> {
                        if(redisRepository.getRedis(l.getId()) == null){
                        redisRepository.saveRedis(
                            RedisLot.builder()
                                .id(l.getId())
                                    .userId(l.getUser().getId())
                                    .lotName(l.getLotName())
                                    .startDate(l.getStartDate())
                                    .endDate(l.getEndDate())
                                    .bidStep(l.getBidStep())
                                    .startingPrice(l.getStartingPrice())
                                    .reservePrice(l.getReservePrice())
                                    .description(l.getDescription())
                                    .itemPictures(l.getItemPictures())
                                    .status(l.getStatus())
                                    .imgs(l.getItemPictures())
                                .endDate(l.getEndDate())
                                .build());
                        log.info("Lot added to redis " + l.getId());
                        }
        });
        }


    public void closeLot(long lotId, UserProfile user){
        Lot lot = bidRepo.getLotById(lotId);

        lot.setLotWinner(user);
        lot.setStatus(3);

        log.warn("Lot closed: " + lot.getId());
        bidRepo.saveLot(lot);
    }

    public void setLotStatus(long lotId, int status){

        Lot lot = bidRepo.getLotById(lotId);

        lot.setStatus(status);

        log.info("Lot status changed: " + lot.getId());
        bidRepo.saveLot(lot);
    }

    public List<LotFrontDto> getAllActiveLotsFront(){
        List<LotFrontDto> listOut = new ArrayList<>();
        Map<Long, RedisLot> allredis = redisRepository.getAllRedis();
        if (allredis.size() >0){
        allredis.values().forEach(
                lot ->{
                   listOut.add(LotFrontDto.getLotFrontDto(lot));
                }
        );
        return listOut;}
        else return null;
    }

    @Override
    public List<LotFrontDto> getWonAuctions(String username){

        List<LotFrontDto> listOut = new ArrayList<>();

        List<Lot> lots = bidRepo.getUsersWonLots(username);


        lots.forEach(l->{
            listOut.add(LotFrontDto.getLotFrontDtoWithoutBidsFirstImage(l));
        });

                return listOut;
    }

    public String closeOverduedLot(Long lotId, String username){

        RedisWaitingPayment red = redisRepository.getPayment(lotId);

        UserProfile user = userService.findByUsername(username).get();

        if (red == null){
            closeLot(lotId, user);
            return "success";
        }

        int purchaseStatus = accountService.purchase(red.getSenderId(), red.getReceiverId(), red.getAmount());

        redisRepository.removeWaitingPayment(lotId);

        if (purchaseStatus <= 0){ //purchase not happened, blocking user
            userService.blockUser(user.getId(), true);
            setLotStatus(red.getId(), -3);
            return "error";
        } else {
            closeLot(red.getId(), user);
            return "success";
        }

    }
}
