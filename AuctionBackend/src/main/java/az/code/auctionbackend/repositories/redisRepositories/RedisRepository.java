package az.code.auctionbackend.repositories.redisRepositories;

import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisUser;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Repository
@Component
public class RedisRepository implements RedisInterface {


    private final String hashReference= "redis";
    private final String hashReferenceUser= "users";

//    private final RedisTemplate redisTemplate;

    @Resource(name = "template")
    private HashOperations<String, Long, RedisLot> hashOperations;

    @Resource(name = "template")
    private HashOperations<String, Long, RedisUser> hashOperationsUser;

    private HashMap<String, Long> users = new HashMap<>();

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


    // User
    @Override
    public Map<Long, RedisUser> getAllRedisUser() {//Long,RedisLot
        return hashOperationsUser.entries(hashReferenceUser);
    }

    @Override
    public RedisUser saveRedisUser(RedisUser red) {
        users.put(red.getUsername(), red.getId());
        hashOperationsUser.putIfAbsent(hashReferenceUser, red.getId(), red);
        return red;
    }

    @Override
    public RedisUser getRedisUser(Long id) {
        return  hashOperationsUser.get(hashReferenceUser,id);
    }

    public RedisUser getRedisUserByUsername(String username) {
        return  getRedisUser(users.get(username));
    }
}
