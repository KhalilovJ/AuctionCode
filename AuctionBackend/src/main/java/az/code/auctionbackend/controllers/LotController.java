package az.code.auctionbackend.controllers;


import az.code.auctionbackend.entities.auction.Lot;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.LotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/api/lots")
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;
    private final RedisRepository redisRepository;

    private final ObjectMapper objectMapper;

    //get all lots

    /**
     * Get all lots from DB
     */
    @GetMapping("/closed")
    public ResponseEntity<List<Lot>> getAllClosedLots() {

        return ResponseEntity.ok(lotService.getAllLots());
    }

    /**
     * Get all active lots from Redis
     */
    @GetMapping("/active")
    public ResponseEntity<Map<Long, RedisLot>> getAllActiveLots() {

        return ResponseEntity.ok(redisRepository.getAllRedis());
    }

    // open lot !

    // place bid -> List<Bid>

    /**
     * Create a new lot
     * @param lot is the new lot
     * @return
     */
    @PostMapping("/lot")
    public ResponseEntity<Lot> save(@RequestBody Lot lot) {

        RedisLot redisLot = objectMapper.convertValue(lot, RedisLot.class);
        // TODO set real ID
        redisLot.setId(new Random().nextLong());

        return (redisRepository.saveRedis(redisLot) == null) ? new ResponseEntity<>(null, HttpStatus.BAD_REQUEST)
                : ResponseEntity.ok(objectMapper.convertValue(redisRepository.saveRedis(redisLot), Lot.class));
    }

    @PutMapping("/update-lot")
    public ModelAndView update(@RequestBody RedisLot lot) {

        return null;
    }

    // send details to winner
}
