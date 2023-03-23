package az.code.auctionbackend.repositories.usersRepositories;

import az.code.auctionbackend.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
public interface UserRepository extends JpaRepository<UserProfile, Long> {


//    @Query(value = "SELECT * FROM profiles", nativeQuery = true)
//    List<UserProfile> getAllUsers();

//    Query(value = "SELECT U FROM UserProfile U WHERE U.username = ?1", nativeQuery = true)
    @Query(value = "SELECT * FROM profiles AS U WHERE U.username= :username", nativeQuery = true)
    Optional<UserProfile> findByUsername(String username);

    @Query(value = "SELECT * FROM profiles AS U WHERE U.id= :id", nativeQuery = true)
    Optional<UserProfile> findById(Long id);

    @Query(value = "select * from profiles as u where u.id in :ids", nativeQuery = true)
    List<UserProfile> findByIds(@Param("ids") List<Long> idList);

}
