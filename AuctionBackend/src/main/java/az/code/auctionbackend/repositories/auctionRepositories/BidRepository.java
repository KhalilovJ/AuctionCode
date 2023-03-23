package az.code.auctionbackend.repositories.auctionRepositories;

import az.code.auctionbackend.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

//    @Query(value = "SELECT * FROM bids", nativeQuery = true)
//    List<Bid> getAllBids();

}
