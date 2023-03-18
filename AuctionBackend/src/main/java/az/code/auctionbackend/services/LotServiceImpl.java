package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisTimer;
import az.code.auctionbackend.repositories.auctionRepositories.AuctionRealtimeRepo;
import az.code.auctionbackend.repositories.auctionRepositories.BidRepository;
import az.code.auctionbackend.repositories.auctionRepositories.LotRepository;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.LotService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private final RedisRepository redisRepository;
    private final AccountServiceImpl accountService;
    private final AuctionRealtimeRepo auctionRealtimeRepo;
    private final UserServiceImpl userService;


    @Override
    @Transactional
    public Lot save(Lot lot) {
        return lotRepository.save(lot);
    }

    @Override
    public List<Lot> getAllLots() {
        return lotRepository.findAll();
    }

    @Override
    public Optional<Lot> findLotById(long id) {
        return lotRepository.findById(id);
    }

    public Lot findRedisLotByIdActive(long id){

        Lot lotMain = auctionRealtimeRepo.getLot(id);

        if (lotMain == null){
            lotMain = findLotById(id).orElse(null);
            auctionRealtimeRepo.addLot(lotMain);
        }
        return lotMain;

    }

    @Transactional
    public void createLot(LotDto lotDto, String images, String username){
        UserProfile user = userService.findByUsername(username).orElse(null);
        Lot lot = lotDto.getLot();
        lot.setItemPictures(images);
        lot.setUser(user);
        lot.setStatus(0);
        // save(lot) - writes data to the database
        Lot tmpLot = save(lot);



        // Мурад, сейв лот даст тебе новый лот, его в редис очередь пихаешь
        // Пихать. Eee Boy
        redisRepository.saveRedis(RedisTimer.builder()
                .id(tmpLot.getId())
                .endDate(tmpLot.getEndDate())
                .build());

        auctionRealtimeRepo.addLot(tmpLot);
    }

    public Lot changeStatus(Long lotId, int status) {
        Lot lot = lotRepository.findById(lotId).get();
        log.error(lot.toString());
        System.out.println("test");
        lot.setStatus(status);

        System.out.println("test2");
        return lot;
    }

    public void closeLot(long lotId) {
        log.error("IN closeLot");
        // 2 - auction finished
        Lot lot = changeStatus(lotId, 2);
        List<Bid> bidList = auctionRealtimeRepo.getLot(lotId).getBidHistory();
        log.error("auctionRealtimeRepo.getLot(lotId).getBidHistory() ");


        // nobody made bids
        if (bidList == null) {
            log.error("nobody made bids");
            bidList = new ArrayList<>();
            lot.setBidHistory(bidList);
        }

        // somebody made bids
        if (!bidList.isEmpty()) {
            log.error("somebody made bids");
            log.error(bidList.toString());
            lot.setBidHistory(bidList);

            Bid winnerBid = getWinnerBid(lot);

            if (winnerBid != null) {
                accountService.purchase(winnerBid.getUser(), lot.getUser(), winnerBid.getBid());
            }
        }

//        log.error(auctionRealtimeRepo.getLot(lotId).toString());

//        Bid winnerBid = getWinnerBid(lot);

        log.error(lot.toString());
        lotRepository.save(lot);

        // как то отправляем клиенту добрую весть :)
        System.out.println();
        // delete from Redis
        redisRepository.deleteRedis(lotId);
        // delete from RAM
        auctionRealtimeRepo.deleteLot(lotId);
    }

    private Bid getWinnerBid(Lot lot){
        Bid bid = lot.getBidHistory().get(0);
        for (Bid bid1: lot.getBidHistory()){
            if (bid1.getBid() > bid.getBid()){
                bid = bid1;
            } else if (bid1.getBid() == bid.getBid() && bid.getBidTime().isAfter(bid1.getBidTime())){

            }
        }
        return bid;
    }

    private Bid getWinnerBidV2(Lot lot) {
        log.error(lot.getBidHistory().toString());

        //TODO validation
        if (lot.getBidHistory().isEmpty()) return null;

        return lot.getBidHistory().get(lot.getBidHistory().size() - 1);
    }

    /**
     * loads all lots with status 0 and 1 from the database into Redis memory if the redis is empty
     */
    @PostConstruct
    private void lotsImport(){

        if(redisRepository.getAllRedis().isEmpty()) {

            lotRepository.getAllNonFinishedLots()
                    .forEach(l -> redisRepository.saveRedis(
                            RedisTimer.builder()
                                .id(l.getId())
                                .endDate(l.getEndDate())
                                .build()));
        }
    }
}
