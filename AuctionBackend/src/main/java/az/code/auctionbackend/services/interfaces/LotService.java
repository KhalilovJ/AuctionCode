package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.entities.auction.Lot;

import java.util.List;

public interface LotService {

    Lot save(Lot lot);

    List<Lot> findAllLots();
}
