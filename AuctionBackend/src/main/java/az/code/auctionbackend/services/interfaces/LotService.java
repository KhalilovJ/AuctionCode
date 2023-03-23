package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.entities.Lot;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LotService {

    Lot save(Lot lot);

    List<Lot> getAllLots();

    @Query(value = "SELECT * FROM lots AS l WHERE l.id = :id", nativeQuery = true)
    Optional<Lot> findLotById(long id);

    Lot changeStatus(Long lotId, int status);
    void closeLot(long lotId);

    void createLot(LotDto lotDto, String images, String username);
}
