package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.entities.auction.Bid;

import java.util.List;

public interface BidService {

    List<Bid> getAllBids();
}
