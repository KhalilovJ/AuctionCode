package az.code.auctionbackend.repositories.redisRepositories;

import az.code.auctionbackend.deserializer.CustomMapper;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisUser;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Repository
@Component
@Slf4j
public class RedisRepository implements RedisInterface {


    private final String hashReference= "redis";
    private final String hashReferenceUser= "users";

    @Resource(name = "template")
    private HashOperations<String, Long, RedisLot> hashOperations;

    @Resource(name = "template")
    private HashOperations<String, Long, RedisUser> hashOperationsUser;

    private final HashMap<String, Long> users = new HashMap<>();
    @Autowired
    private CustomMapper mapper;

    @PostConstruct
    public void work() {

        getAllRedisUser()
                .values()
                .forEach(red -> users.put(red.getUsername(), red.getId()));
    }

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
        log.error("getRedisUser " + id);
        return  hashOperationsUser.get(hashReferenceUser,id);
    }

    public RedisUser getRedisUserByUsername(String username) {
        log.info("getRedisUserByUsername username " + username);
        log.info("getRedisUserByUsername users.get(username) " + users.get(username));
        return  getRedisUser(users.get(username));
    }

    public void importUser(UserProfile userProfile) {

        saveRedisUser(mapper.mapperUserProfileToRedisUser(userProfile));
    }
}
