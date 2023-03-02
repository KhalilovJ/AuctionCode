package az.code.auctionbackend.repositories;

import az.code.auctionbackend.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAll();
}
