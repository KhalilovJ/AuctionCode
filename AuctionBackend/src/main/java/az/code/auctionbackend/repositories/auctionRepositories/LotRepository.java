package az.code.auctionbackend.repositories.auctionRepositories;

import az.code.auctionbackend.entities.auction.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot, Long> {
}
