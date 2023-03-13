package az.code.auctionbackend.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduleService {

    @Scheduled(fixedDelay = 3000)
    public void test() {

        System.out.println("test " + LocalDateTime.now());
    }
}
