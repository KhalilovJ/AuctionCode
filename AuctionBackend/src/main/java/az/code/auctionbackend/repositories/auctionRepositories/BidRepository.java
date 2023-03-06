package az.code.auctionbackend.repositories.auctionRepositories;

import az.code.auctionbackend.entities.auction.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAll();
}
