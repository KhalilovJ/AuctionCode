package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.DTOs.BidResponseDto;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.auctionRepositories.AuctionRealtimeRepo;
import az.code.auctionbackend.repositories.auctionRepositories.BidRepository;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.BidService;
import az.code.auctionbackend.services.interfaces.LotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    // TODO delete
    private final AuctionRealtimeRepo auctionRealtimeRepo;

private final RedisRepository redisRepository;
private ObjectMapper objectMapper;
    @Override
    public List<Bid> getAllBids() {
        return bidRepository.findAll();
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


    public Bid makeBid(String username, Long lotId, double bidValue){

//        Lot lot = auctionRealtimeRepo.getLot(lotId);
        RedisLot redisLot = redisRepository.getRedis(lotId);
        Lot lot = objectMapper.convertValue(redisLot, Lot.class);
        log.info("Lot realtime found " + lot.getId());

        UserProfile user = userService.findByUsername(username).orElse(null);

        LocalDateTime time = LocalDateTime.now();

        Bid bid = Bid.builder()
                .user(user)
                .lot(lot)
                .bidTime(time)
                .bid(bidValue)
                .build();

        BidDto bidDto = BidDto.builder()
                .userId(user.getId())
                .bid(bidValue)
                .lotId(lotId)
                .bidTime(time)
                .build();

        List<BidDto> bidList = redisLot.getBidHistory();

        bidList.add(bidDto);
        redisLot.setBidHistory(bidList);

//        redisRepository.updateRedis(redisLot);
        auctionRealtimeRepo.makeBid(lotId, bid);

        return bid;
    }


    public BidResponseDto bidDtoMapper(Bid bid){

//        return BidResponseDto.builder()
//                .lotId(bid.getLot().getId())
//                .userId(bid.getUser().getId())
//                .username(bid.getUser().getUsername())
//                .lotCurrentBidPrice(bid.getLot().getCurrentBid())
//                .bid(bid.getBid())
//                .bidTime(bid.getBidTime())
//                .build();
        return BidResponseDto.builder()
                .lotId(bid.getLot().getId())
                .userId(bid.getUser().getId())
                .username(bid.getUser().getUsername())
                .lotCurrentBidPrice(bid.getLot().getCurrentBid())
                .bid(bid.getBid())
                .bidTime(bid.getBidTime())
                .build();
    }
}
