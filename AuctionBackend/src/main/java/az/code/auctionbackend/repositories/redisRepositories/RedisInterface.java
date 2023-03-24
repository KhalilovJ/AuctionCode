package az.code.auctionbackend.repositories.redisRepositories;//package az.code.telegrambot.controllers.redis;

import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisUser;
import az.code.auctionbackend.entities.redis.RedisWaitingPayment;

import java.util.Map;

public interface RedisInterface {

        RedisLot saveRedis(RedisLot red);
        RedisLot getRedis(Long id);
        void updateRedis(RedisLot red);
        Map<Long, RedisLot> getAllRedis();//Long,RedisLot
        void deleteRedis(Long id);
        void saveAllRedis(Map<Long, RedisLot> map);

        // Waiting payments
        Map<Long, RedisWaitingPayment> getAllWaitingPayments();
        void addWaitingPayment(Long id, RedisWaitingPayment payment);
        void removeWaitingPayment(Long id);
}
