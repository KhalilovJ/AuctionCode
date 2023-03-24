package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.DTOs.BidResponseDto;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.auctionRepositories.BidRepo;
import az.code.auctionbackend.repositories.auctionRepositories.BidRepository;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.BidService;
import az.code.auctionbackend.services.interfaces.LotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class BidServiceImpl implements BidService {

    // Repositories
    private final BidRepository bidRepository;
    // Services
    private final LotService lotService;
    private final UserServiceImpl userService;
    private final BidRepo bidRepo;

private final RedisRepository redisRepository;
private ObjectMapper objectMapper;
    @Override
    public List<Bid> getAllBids() {
        return bidRepo.getAllBids();
    }

    @Override
    public Bid saveBid(Bid bid) {

        Bid bidOut = Bid.builder()
                .lot(lotService.findLotById(bid.getLot().getId()).get())
                .user(userService.findProfileById(bid.getUser().getId()).get())
                .bid(bid.getBid())
                .bidTime(LocalDateTime.now())
                .build();

//        bidController.sendUpdates(bidOut);

        return bidRepository.save(bidOut);

    }


    public BidDto makeBid(String username, Long lotId, double bidValue){

        RedisLot redisLot = redisRepository.getRedis(lotId);

        if (redisLot != null && redisLot.getEndDate().isAfter(LocalDateTime.now())){ // if it's null lot is closed and we can't make bids

        log.info("Lot realtime found " + redisLot.getId());

        UserProfile user = userService.findByUsername(username).get();

        if (user.isBlocked()){
            log.error("Can't place bid. User locked. Username: " + username + " Lot id: " + lotId);
            return null;
        }

        BidDto bidDto = BidDto.builder()
                .userId(user.getId())
                .username(username)
                .bid(bidValue)
                .lotId(lotId)
                .bidTime(LocalDateTime.now())
                .build();

        log.info("makeBid bidDto " + bidDto);

        List<BidDto> bidList = redisLot.getBidHistory();

        if (bidList == null) bidList = new ArrayList<>();

        log.info("Bid: " + bidDto);
        bidList.add(bidDto);
        redisLot.setBidHistory(bidList);

        if (bidValue > redisLot.getCurrentBid()){
            redisLot.setCurrentBid(bidValue);
        }

        redisRepository.updateRedis(redisLot);

        return bidDto;
        } else {
            log.error("Can't place bid. Lot is not active. Username: " + username + " Lot id: " + lotId);
            return null;
        }
    }


    public BidResponseDto bidDtoMapper(BidDto bidDto){

//        return BidResponseDto.builder()
//                .lotId(bid.getLot().getId())
//                .userId(bid.getUser().getId())
//                .username(bid.getUser().getUsername())
//                .lotCurrentBidPrice(bid.getLot().getCurrentBid())
//                .bid(bid.getBid())
//                .bidTime(bid.getBidTime())
//                .build();
        long lotId = bidDto.getLotId();

        return BidResponseDto.builder()
                .lotId(lotId)
                .userId(bidDto.getUserId())
                .username(bidDto.getUsername())
                .lotCurrentBidPrice(redisRepository.getRedis(lotId).getCurrentBid())
                .bid(bidDto.getBid())
                .bidTime(bidDto.getBidTime())
                .build();

//        return BidResponseDto.builder()
//                .lotId(bid.getLot().getId())
//                .userId(bid.getUser().getId())
//                .username(bid.getUser().getUsername())
//                .lotCurrentBidPrice(bid.getLot().getCurrentBid())
//                .bid(bid.getBid())
//                .bidTime(bid.getBidTime())
//                .build();
    }


    @PostConstruct
    private void makeBidsMock() throws InterruptedException {
        Thread.sleep(100);
        makeBid("malishov", 1l, 600);
        makeBid("Khalil", 1l, 700);
        makeBid("admin6", 1l, 800);
        makeBid("user8", 1l, 950000);

    }

}
