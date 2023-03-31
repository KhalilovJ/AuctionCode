package az.code.auctionbackend.repositories.redisRepositories;

import az.code.auctionbackend.deserializer.CustomMapper;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisUser;
import az.code.auctionbackend.entities.redis.RedisWaitingPayment;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//@Repository
@Component
@Slf4j
public class RedisRepository implements RedisInterface {


    private final String hashReference= "redis";

    private final String getHashReferenceWaitings = "waitings";
    private final String hashReferenceUser= "users";
    private final String hashReferenceWaitings= "waitings";

    @Resource(name = "template")
    private HashOperations<String, Long, RedisLot> hashOperations;

    @Resource(name = "template")
    private HashOperations<String, Long, RedisWaitingPayment> waitingsHash;

//    @Resource(name = "template")
//    private HashOperations<String, Long, RedisUser> hashOperationsUser;

    private final HashMap<String, Long> users = new HashMap<>();
    @Autowired
    private CustomMapper mapper;

//    @PostConstruct
//    public void work() {
//
//        getAllRedisUser()
//                .values()
//                .forEach(red -> users.put(red.getUsername(), red.getId()));
//    }

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
    public void updateStatus(long lotId, int status){
        RedisLot lot = getRedis(lotId);
        lot.setStatus(status);
        updateRedis(lot);
    }

    @Override
    public void deleteRedis(Long id) {
        hashOperations.delete(hashReference,id);
    }

    @Override
    public void saveAllRedis(Map<Long, RedisLot> map) {
        hashOperations.putAll(hashReference,map);
    }

    @Override
    public void updateRedisLotEndTime(long lotId, LocalDateTime time){
        RedisLot lot = getRedis(lotId);
        lot.setEndDate(time);
        updateRedis(lot);
    }


    // Waiting payments

    @Override
    public Map<Long, RedisWaitingPayment> getAllWaitingPayments() {
        return waitingsHash.entries(hashReferenceWaitings);
    }

    @Override
    public void addWaitingPayment(RedisWaitingPayment payment) {
        waitingsHash.putIfAbsent(hashReferenceWaitings,payment.getId(),payment);
    }

    @Override
    public void removeWaitingPayment(Long id) {
        waitingsHash.delete(hashReferenceWaitings,id);
    }

    @Override
    public RedisWaitingPayment getPayment(long id)  {
        return  waitingsHash.get(hashReferenceWaitings,id);
    }


}
