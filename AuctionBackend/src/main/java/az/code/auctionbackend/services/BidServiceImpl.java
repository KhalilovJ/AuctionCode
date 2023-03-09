package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.auction.Bid;
import az.code.auctionbackend.entities.auction.Lot;
import az.code.auctionbackend.repositories.auctionRepositories.BidRepository;
import az.code.auctionbackend.services.interfaces.BidService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BidServiceImpl implements BidService {

    // Repositories
    BidRepository bidRepository;
    // Services
    LotServiceImpl lotService;
    UserServiceImpl userService;

    @Override
    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    @Override
    public Bid saveBid(Bid bid) {

        return bidRepository.save(Bid.builder()
                .lot(lotService.findLotById(bid.getLot().getId()).get())
                .user(userService.findProfileById(bid.getUser().getId()).get())
                .bid(bid.getBid())
                .bidTime(LocalDate.now())
                .build());

    }
}
