package az.code.auctionbackend.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduleService {

    @Scheduled(cron = "${interval-in-cron-every-minute}")
    public void test() throws InterruptedException {

        System.out.println("test " + LocalDateTime.now());
    }

}
