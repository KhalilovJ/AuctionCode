package az.code.auctionbackend.repositories;

import az.code.auctionbackend.entities.users.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT u FROM UserProfile u")
    List<UserProfile> findAll();


    Optional<UserProfile> findByUsername(String Username);
}
