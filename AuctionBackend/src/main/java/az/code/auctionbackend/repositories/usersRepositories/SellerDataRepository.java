package az.code.auctionbackend.repositories.usersRepositories;

import az.code.auctionbackend.entities.SellerData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerDataRepository extends JpaRepository<SellerData, Long> {
}
