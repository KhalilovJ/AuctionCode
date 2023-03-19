package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisTimer;
import az.code.auctionbackend.repositories.auctionRepositories.AuctionRealtimeRepo;
import az.code.auctionbackend.repositories.auctionRepositories.BidRepository;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.BidService;
import az.code.auctionbackend.services.interfaces.LotService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {


    private final LotService lotService;
    private final RedisRepository redisRepository;
    private final BidService bidService;

     @PostConstruct
     public void work() {

         LotDto lotDto = LotDto.builder()
                 .bidStep(1)
                 .description("test")
                 .endDate(LocalDateTime.now().plusMinutes(2))
                 .build();
         System.out.println("lotDto!!! " + lotDto.getEndDate());
 //        lotService.createLot(lotDto, null, "test");


         lotService.createLot(lotDto, null, "user6");
     }

    @Scheduled(cron = "${interval-in-cron-every-minute}")
    public void scheduledRateChecker() {
        checkTimer(redisRepository.getAllRedis().values().stream().toList());
    }

    public void checkTimer(List<RedisTimer> redisTimerList) {

        log.info("Redis checking " + LocalDateTime.now());
//        System.out.println("Куда идём мы с Пятачком - большой-большой секрет! " + LocalDateTime.now());

        for (RedisTimer l : redisTimerList) {

            LocalDateTime endTime = l.getEndDate().truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

//            System.out.println(l);
            log.info(l.toString());
            //|| endTime.isBefore(currentTime)
            if (endTime.isEqual(currentTime) ||endTime.isBefore(currentTime) ) {

                long lotId = l.getId();
                Lot lot = lotService.findLotById(lotId).get();

                //TODO @Jamal
                // Предлагаю в Лот держать CurrentBid в виде Bid Entity
                // Таким образом будем знать кто сделал последнюю ставку

                log.error("lotId " + lotId);
                lotService.closeLot(lotId);

                log.info("Scheduler checked " + LocalDateTime.now() + " " + l.getId());
            }
        }
    }

    @PostConstruct
    private void init(){
        lotService.closeLot(1);
    }

}
