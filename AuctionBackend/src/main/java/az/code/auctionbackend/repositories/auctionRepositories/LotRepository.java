package az.code.auctionbackend.repositories.auctionRepositories;

import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LotRepository extends JpaRepository<Lot, Long> {

//    @Query("SELECT l FROM Lot l WHERE l.status = 0 OR l.status = 1")
    @Query(value = "SELECT * FROM lots AS l WHERE l.status = 0 or l.status =1", nativeQuery = true)
    List<Lot> getAllNonFinishedLots();

    @Query(value = "SELECT * FROM lots AS l WHERE l.id = :id", nativeQuery = true)
    Optional<Lot> findById(Long id);

//    @Query(value = "SELECT * FROM lots", nativeQuery = true)
//    List<Lot> getAllLots();

}
