package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.entities.Lot;

import java.util.List;
import java.util.Optional;

public interface LotService {

    Lot save(Lot lot);

    List<Lot> getAllLots();

    Optional<Lot> findLotById(long id);

    Lot changeStatus(Long lotId, int status);
    void closeLot(long lotId);
}
