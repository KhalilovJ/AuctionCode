package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.auction.Bid;
import az.code.auctionbackend.entities.auction.Lot;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.auctionRepositories.LotRepository;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.LotService;
import com.rabbitmq.tools.json.JSONUtil;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LotServiceImpl implements LotService {

    LotRepository lotRepository;

    RedisRepository redisRepository;

    @Override
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

//    @PostConstruct
//    public void testBid() {
//
//        System.out.println("PostConstruct test");
//        redisRepository.saveRedis(RedisLot.builder().id(1l).bidStep(10).description("test").bidHistory(new ArrayList<>()).build());
//        newBid(1L, Bid.builder().bid(5).bidTime(LocalDateTime.now()).build());
//        newBid(1L, Bid.builder().bid(10).bidTime(LocalDateTime.now()).build());
//        newBid(1L, Bid.builder().bid(15).bidTime(LocalDateTime.now()).build());
//
//        newBid(2L, Bid.builder().bid(15).bidTime(LocalDateTime.now()).build());
//            redisRepository.getRedis(1L);
//    }

    public RedisLot newBid(long lotId, Bid bid) {

        RedisLot lot = redisRepository.getRedis(lotId);
        if (lot == null) {
            // TODO Validation
            return null;
        }
        List<Bid> bids = lot.getBidHistory();

        bids.add(bid);
        lot.setBidHistory(bids);
        redisRepository.updateRedis(lot);

        return lot;
    }
}
