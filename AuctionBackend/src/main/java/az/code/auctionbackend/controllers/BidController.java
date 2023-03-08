package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.deserializer.BidCustomDeserializer;
import az.code.auctionbackend.entities.auction.Bid;
import az.code.auctionbackend.services.interfaces.BidService;
import az.code.auctionbackend.services.interfaces.LotService;
import az.code.auctionbackend.services.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    @Autowired
    private ApplicationContext context;
    private final BidService bidService;
//private final LotService lotService;
//private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<BidDto>> getAllBids() {

        return ResponseEntity.ok(bidService.getAllBids().stream()
                .map(x -> objectMapper.convertValue(x, BidDto.class))
                .collect(Collectors.toList()));
    }

    @PostMapping("/bid")
    public ResponseEntity<BidDto> saveBid(@RequestBody BidDto bid) {

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Bid.class, new BidCustomDeserializer(context));
        objectMapper.registerModule(simpleModule);

        //TODO fix bug

        bid.setBidTime(LocalDate.now());
        System.out.println(bid);

        System.out.println(objectMapper.convertValue(bid, Bid.class));
        bidService.saveBid(objectMapper.convertValue(bid, Bid.class));


        System.out.println(bidService.getAllBids());
        return null;
    }
}
