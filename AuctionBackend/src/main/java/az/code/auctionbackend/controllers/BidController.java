package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.services.interfaces.BidService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<BidDto>> getAllBids() {

        return ResponseEntity.ok(bidService.getAllBids().stream()
                .map(x -> objectMapper.convertValue(x, BidDto.class))
                .collect(Collectors.toList()));
    }
}
