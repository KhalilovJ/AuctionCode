package az.code.auctionbackend.repositories.usersRepositories;

import az.code.auctionbackend.entities.SellerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SellerDataRepository extends JpaRepository<SellerData, Long> {


    @Query("SELECT S FROM SellerData S WHERE S.userProfile.username = ?1")
    SellerData findByUsername(String username);


}
