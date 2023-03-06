package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.auction.Bid;
import az.code.auctionbackend.repositories.auctionRepositories.BidRepository;
import az.code.auctionbackend.services.interfaces.BidService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BidServiceImpl implements BidService {

    BidRepository bidRepository;

    @Override
    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }
}
