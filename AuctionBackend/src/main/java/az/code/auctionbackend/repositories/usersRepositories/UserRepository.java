package az.code.auctionbackend.repositories.usersRepositories;

import az.code.auctionbackend.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfile, Long> {


    @Query("SELECT u FROM UserProfile u")
    List<UserProfile> findAll();


    Optional<UserProfile> findByUsername(String username);

    Optional<UserProfile> findById(Long id);

    @Query("select u from UserProfile u where u.id in :ids")
    List<UserProfile> findByIds(@Param("ids") List<Long> idList);

}
