package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.auction.Lot;
import az.code.auctionbackend.repositories.auctionRepositories.LotRepository;
import az.code.auctionbackend.services.interfaces.LotService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LotServiceImpl implements LotService {

    LotRepository lotRepository;

    @Override
    public Lot save(Lot lot) {
        return lotRepository.save(lot);
    }

    @Override
    public List<Lot> findAllLots() {
        return lotRepository.findAll();
    }
}
