package az.code.auctionbackend.services;

import org.springframework.stereotype.Service;

@Service
public class TaskDefinitionBean implements Runnable {


    @Override
    public void run() {
        System.out.println("LOL");
    }


}
