package az.code.auctionbackend.repositories;

import az.code.auctionbackend.entities.auction.Bid;

import java.util.HashMap;

public class RedisRepository {

    HashMap<Long, Bid> bidTempRepo = new HashMap<>();
}
