package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
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
        Lot lot = changeStatus(lotId, 2);
//        changeStatus2(lotId, 2);
//        Lot lot = bidRepo.getLotById(lotId);

        RedisLot redisLot = redisRepository.getRedis(lotId);
        List<BidDto> bidDtoList = redisLot.getBidHistory();

        List<Bid> bidList = new ArrayList<>();

        log.info("redisLot.getBidHistory() - done");
        log.info("bidList " + bidDtoList);

        // nobody made bids
        if (bidDtoList == null) {
            log.info("nobody made bids");
            bidDtoList = new ArrayList<>();
            lot.setBidHistory(bidList);
        }

        // somebody made bids
        if (!bidDtoList.isEmpty()) {
            log.info("somebody made bids");
            log.info("Bids: " + bidDtoList);

            Lot lot1 = bidRepo.getLotById(bidDtoList.get(0).getLotId());

            for (BidDto bidDto : bidDtoList) {
                bidList.add(Bid.builder()
                        .lot(lot1)
                        .bid(bidDto.getBid())
                        .user(userService.findProfileById(bidDto.getUserId()).get())
                        .bidTime(bidDto.getBidTime())
                        .build());
            }

            lot.setBidHistory(bidList);

            Bid winnerBid = getWinnerBid(lot);

            // TODO fix bug -.IndexOutOfBoundsException
            if (winnerBid != null) {
                long winnerAccId = winnerBid.getUser().getAccount().getId();
                long sellerAccId = lot.getUser().getAccount().getId();
                double winnerBid1 = winnerBid.getBid();
                accountService.purchase(
                        winnerAccId,
                        sellerAccId,
                        winnerBid1
                );
            }
        }

        log.warn(lot.toString());
//      save lot to DB
//      lotRepository.save(lot);
        bidRepo.saveLot(lot);

        // как то отправляем клиенту добрую весть :)
        System.out.println("WINNER!");
        // delete from Redis
        redisRepository.deleteRedis(lotId);
    }

    private Bid getWinnerBid(Lot lot){
        Bid bid = lot.getBidHistory().get(0);
        for (Bid bid1: lot.getBidHistory()){
            if (bid1.getBid() > bid.getBid()){
                bid = bid1;
            } else if (bid1.getBid() == bid.getBid() && bid.getBidTime().isAfter(bid1.getBidTime())){

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

        if(redisRepository.getAllRedis().isEmpty()) {

            lotRepository.getAllNonFinishedLots()
                    .forEach(l -> redisRepository.saveRedis(
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
                                .endDate(l.getEndDate())
                                .build()));
        }

    }
}
