package az.code.auctionbackend.repositories.redisRepositories;//package az.code.telegrambot.controllers.redis;

import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisTimer;

import java.util.Map;

public interface RedisInterface {

        RedisTimer saveRedis(RedisTimer red);
        RedisTimer getRedis(Long id);
        void updateRedis(RedisTimer red);
        Map<Long, RedisTimer> getAllRedis();//Long,RedisLot
        void deleteRedis(Long id);
        void saveAllRedis(Map<Long, RedisTimer> map);
}
