package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisTimer;
import az.code.auctionbackend.repositories.auctionRepositories.AuctionRealtimeRepo;
import az.code.auctionbackend.repositories.auctionRepositories.LotRepository;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.LotService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private final RedisRepository redisRepository;

    private final AuctionRealtimeRepo auctionRealtimeRepo;
    private final UserServiceImpl userService;


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

    public Lot findRedisLotByIdActive(long id){

        Lot lotMain = auctionRealtimeRepo.getLot(id);

        if (lotMain == null){
            lotMain = findLotById(id).orElse(null);
            auctionRealtimeRepo.addLot(lotMain);
        }
        return lotMain;

    }

    public void createLot(LotDto lotDto, String images, String username){
        UserProfile user = userService.findByUsername(username).orElse(null);
        Lot lot = lotDto.getLot();
        lot.setItemPictures(images);
        lot.setUser(user);
        Lot tmpLot = save(lot);

        // Мурад, сейв лот даст тебе новый лот, его в редис очередь пихаешь
        // Пихать. Eee Boy
        redisRepository.saveRedis(RedisTimer.builder()
                .id(tmpLot.getId())
                .endDate(tmpLot.getEndDate())
                .build());
    }
}
