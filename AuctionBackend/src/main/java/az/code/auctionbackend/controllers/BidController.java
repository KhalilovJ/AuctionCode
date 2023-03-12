package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.DTOs.BidResponseDto;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.services.BidServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/open/api/bids")
@RequiredArgsConstructor
@Log4j2
public class BidController {

//    @Autowired
//    private ApplicationContext context;

    @Autowired
    private final BidServiceImpl bidService;
//private final LotService lotService;
//private final UserService userService;
    private final ObjectMapper objectMapper;

    HashMap<Long, List<SseEmitter>> subscribers = new HashMap<>();

//    @PostConstruct
//    private void init(){
//        bidService.setBidController(this);
//    }


    @CrossOrigin
    @GetMapping("/{lotId}")
    public SseEmitter subscribeToLot(@PathVariable Long lotId){
        SseEmitter emitter = new SseEmitter(86400000l);

        try {
            emitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (subscribers.get(lotId) == null){
            subscribers.put(lotId, new CopyOnWriteArrayList());
        }
        subscribers.get(lotId).add(emitter);

        emitter.onCompletion(() -> {
            log.info("Emitter completed: {}", emitter);
            subscribers.get(lotId).remove(emitter);
        });

        emitter.onTimeout(() -> {
            log.info("Emitter timed out: {}", emitter);
            emitter.complete();
            subscribers.get(lotId).remove(emitter);
        });

        return emitter;
    }

    @PostMapping("/makeBid")
    public String makeBid(@AuthenticationPrincipal UserDetails userIn, @RequestBody JSONObject jsonRequest){
        System.out.println(jsonRequest);
        Long lotId = jsonRequest.getLong("lotId");
        double bidValue = jsonRequest.getLong("bid");

        Bid bid = bidService.makeBid(userIn.getUsername(), lotId, bidValue);

        sendUpdates(bidService.bidDtoMapper(bid));

        log.info("Bid placed; Lot Id is: " + lotId);
        return "success";
    }


    public void sendUpdates(BidResponseDto bid){

        List<SseEmitter> emittersList = subscribers.get(bid.getLotId());

        for (SseEmitter emitter: emittersList){
            try {

                emitter.send(SseEmitter.event().name("bid").data(bid));
            } catch (IOException  e) {
                emittersList.remove(emitter);
            }
        }

    }






    @GetMapping
    public ResponseEntity<List<BidDto>> getAllBids() {

        return ResponseEntity.ok(bidService.getAllBids().stream()
                .map(x -> objectMapper.convertValue(x, BidDto.class))
                .collect(Collectors.toList()));
    }

//    @PostMapping("/bid")
//    public ResponseEntity<BidDto> saveBid(@RequestBody BidDto bid) {
//
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addDeserializer(Bid.class, new BidCustomDeserializer(context));
//        objectMapper.registerModule(simpleModule);
//
//        //TODO fix bug
//
//        bid.setBidTime(LocalDate.now());
//        System.out.println(bid);
//
//        System.out.println(objectMapper.convertValue(bid, Bid.class));
//        bidService.saveBid(objectMapper.convertValue(bid, Bid.class));
//
//
//        System.out.println(bidService.getAllBids());
//        return null;
//    }
}
