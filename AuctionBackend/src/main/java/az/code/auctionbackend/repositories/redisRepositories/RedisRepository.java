package az.code.auctionbackend.repositories.redisRepositories;

import az.code.auctionbackend.entities.auction.Bid;
import az.code.auctionbackend.entities.redis.RedisLot;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Repository
@Component
public class RedisRepository implements RedisInterface {


    HashMap<Long, Bid> bidTempRepo = new HashMap<>();
    private final String hashReference= "redis";

//    private final RedisTemplate redisTemplate;

    @Resource(name = "template")
    private HashOperations<String, Long, RedisLot> hashOperations;

    @Override
    public RedisLot saveRedis(RedisLot red) {
        hashOperations.putIfAbsent(hashReference,red.getId(),red);
        return red;
    }

    //Camalin  dediyi methodlar
    @Override
    public RedisLot getRedis(Long chatId) {
        return  hashOperations.get(hashReference,chatId);
    }

    @Override
    public void updateRedis(RedisLot red) {
        hashOperations.put(hashReference, red.getId(), red);
    }

   @Override
   public Map<Long, RedisLot> getAllRedis() {//Long,RedisLot
       return hashOperations.entries(hashReference);
    }

    @Override
    public void deleteRedis(Long id) {
        hashOperations.delete(hashReference,id);
    }


    @Override
    public void saveAllRedis(Map<Long, RedisLot> map) {
        hashOperations.putAll(hashReference,map);
    }



}
