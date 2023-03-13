package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.entities.redis.RedisTimer;
import az.code.auctionbackend.repositories.auctionRepositories.AuctionRealtimeRepo;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.LotService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
public class ScheduleService {

    @Autowired
    LotService lotService;

    @Autowired
    RedisRepository redisRepository;

    List<RedisTimer> redisTimerList = new ArrayList<>();

    @PostConstruct
    public void work() {

        redisRepository.saveRedis(RedisTimer.builder().id(1).endDate(LocalDateTime.now().plusMinutes(1)).build());
        redisRepository.saveRedis(RedisTimer.builder().id(2).endDate(LocalDateTime.now().plusMinutes(2)).build());
        redisRepository.saveRedis(RedisTimer.builder().id(3).endDate(LocalDateTime.now().plusMinutes(3)).build());

        redisTimerList = redisRepository.getAllRedis().values().stream().toList();
        System.out.println(redisTimerList);

//        list.add(Lot.builder().description("lot 1").endDate(LocalDateTime.now().plusMinutes(1)).build());
//        list.add(Lot.builder().description("lot 2").endDate(LocalDateTime.now().plusMinutes(2)).build());
//        list.add(Lot.builder().description("lot 3").endDate(LocalDateTime.now().plusMinutes(3)).build());

//        lotService.save(Lot.builder()
//                .bidStep(5)
//                .description("lot 3")
//                .endDate(LocalDateTime.now().plusMinutes(3))
//                .reservePrice(25)
//                .startDate(LocalDateTime.now().minusDays(5))
//                .startingPrice(10)
//                .build());
    }

    @Scheduled(cron = "${interval-in-cron-every-minute}")
    public void scheduledRateChecker() {
        checkTimer(redisTimerList);
    }

    public void checkTimer(List<RedisTimer> redisTimerList) {


        for (RedisTimer l : redisTimerList) {

            LocalDateTime endTime = l.getEndDate().truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

            System.out.println(l);
//            if (endTime.isEqual(currentTime)) {
//
//                System.out.println("\t" + l.getEndDate() + " BOOM");
//            }
        }
    }


//    @Autowired
//    private TaskScheduler taskScheduler;
//
//    Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();
//
//    public void scheduleATask(String jobId, Runnable tasklet, String cronExpression) {
//        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);
//        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet,
//                new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
//        jobsMap.put(jobId, scheduledTask);
//    }
//
//    public void removeScheduledTask(String jobId) {
//        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
//        if(scheduledTask != null) {
//            scheduledTask.cancel(true);
//            jobsMap.put(jobId, null);
//        }
//    }

}
