package az.code.auctionbackend.repositories.redis;//package az.code.telegrambot.controllers.redis;

import az.code.telegrambot.entity.RedisChat;

import java.util.Map;

public interface RedisInterface {

        RedisChat saveRedis(RedisChat red);
        RedisChat getRedis(Long id);
        RedisChat saveRedisWithChatId(RedisChat red,Long chatId) ;
        void updateRedis(RedisChat red);
        Map<Long, RedisChat> getAllRedis();//Long,RedisChat
        void deleteRedis(Long id);
        void saveAllRedis(Map<Long, RedisChat> map);
}
