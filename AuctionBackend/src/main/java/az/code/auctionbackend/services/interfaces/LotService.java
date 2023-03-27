package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.DTOs.LotFrontDto;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
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
    void closeLot(long lotId, UserProfile userId);
    void setLotStatus(long lotId, int status);
    List<LotFrontDto> getAllActiveLotsFront();
    List<LotFrontDto> getWonAuctions(String username);
    String closeOverduedLot(Long lotId, String username);
}
