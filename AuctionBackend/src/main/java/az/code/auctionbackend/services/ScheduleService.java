package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisWaitingPayment;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.AccountService;
import az.code.auctionbackend.services.interfaces.LotService;
import az.code.auctionbackend.services.interfaces.UserService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ScheduleService {

    private final LotService lotService;
    private final RedisRepository redisRepository;
    private final AccountService accountService;
    private final UserService userService;

    @Scheduled(cron = "${interval-in-cron-every-minute}")
    public void scheduledRateChecker() {

        log.info("Scheduled work... Time: " + LocalDateTime.now());

        checkTimer(redisRepository.getAllRedis().values().stream().toList());

        checkWaitingPayment(redisRepository.getAllWaitingPayments().values().stream().toList());
    }

    public void checkTimer(List<RedisLot> redisTimerList) {


        for (RedisLot l : redisTimerList) {

            LocalDateTime endTime = l.getEndDate().truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

            log.info(l.toString());
            //|| endTime.isBefore(currentTime)
            if (endTime.isEqual(currentTime) ||endTime.isBefore(currentTime) ) {

                long lotId = l.getId();
//                Lot lot = lotService.findLotById(lotId).get();

                log.error("lotId " + lotId);
                lotService.closeLot(lotId);

                log.info("Scheduler checked " + LocalDateTime.now() + " " + l.getId());
            }
        }
    }


    private void checkWaitingPayment(List<RedisWaitingPayment> waitingPayments){

        for (RedisWaitingPayment red: waitingPayments){
            if (
                            red.getCreationTime().plusHours(24l).isAfter(LocalDateTime.now()) ||
                            red.getCreationTime().plusHours(24l).isEqual(LocalDateTime.now())
            ){ // payment is overdue
                log.info("Payment overdue: User id: " + red.getSenderId() + " ; Amount: " + red.getAmount());

                int purchaseStatus = accountService.purchase(red.getSenderId(), red.getReceiverId(), red.getAmount());

                UserProfile user = accountService.getAccountDetails(red.getSenderId()).getUser();
                    if (purchaseStatus <= 0){ //purchase not happened, blocking user
                        userService.blockUser(user.getId(), true);
                        lotService.setLotStatus(red.getId(), -3);
                    } else {
                        lotService.closeLot(red.getId(), user);
                    }

                    redisRepository.removeWaitingPayment(red.getId());
            }
        }


    }

//    @PostConstruct
//    private void init(){
//        try {
//            Thread.sleep(3000);
//
//            lotService.closeLot(1);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
