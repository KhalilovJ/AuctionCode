package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.entities.auction.Bid;

import java.util.List;
import java.util.Optional;

public interface BidService {

    List<Bid> getAllBids();

    Bid saveBid(Bid bid);
}
