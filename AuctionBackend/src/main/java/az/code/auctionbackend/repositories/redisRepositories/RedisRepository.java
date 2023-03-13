package az.code.auctionbackend.repositories.redisRepositories;

import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisTimer;
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
    private HashOperations<String, Long, RedisTimer> hashOperations;

    @Override
    public RedisTimer saveRedis(RedisTimer red) {
        hashOperations.putIfAbsent(hashReference,red.getId(),red);
        return red;
    }

    //Camalin  dediyi methodlar
    @Override
    public RedisTimer getRedis(Long chatId) {
        return  hashOperations.get(hashReference,chatId);
    }

    @Override
    public void updateRedis(RedisTimer red) {
        hashOperations.put(hashReference, red.getId(), red);
    }

   @Override
   public Map<Long, RedisTimer> getAllRedis() {//Long,RedisLot
       return hashOperations.entries(hashReference);
    }

    @Override
    public void deleteRedis(Long id) {
        hashOperations.delete(hashReference,id);
    }


    @Override
    public void saveAllRedis(Map<Long, RedisTimer> map) {
        hashOperations.putAll(hashReference,map);
    }



}
