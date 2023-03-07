package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.auction.Lot;
import az.code.auctionbackend.repositories.auctionRepositories.LotRepository;
import az.code.auctionbackend.services.interfaces.LotService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LotServiceImpl implements LotService {

    LotRepository lotRepository;

    @Override
    public Lot save(Lot lot) {
        return lotRepository.save(lot);
    }

    @Override
    public List<Lot> getAllLots() {
        return lotRepository.findAll();
    }

    @Override
    public Optional<Lot> findLotById(long id) {
        return lotRepository.findById(id);
    }
}
