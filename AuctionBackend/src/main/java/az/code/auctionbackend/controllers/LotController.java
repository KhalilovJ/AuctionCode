package az.code.auctionbackend.controllers;


import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.LotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/open/api/lots")
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


    @CrossOrigin
    @GetMapping("/{lotId}")
    public Lot getLot(@PathVariable Long lotId){

//        ModelAndView model = new ModelAndView("auction");
        Lot lot = lotService.findLotById(lotId).orElse(null);

//        model.addObject("auction", lot);

        return lot;
    }

    // send details to winner

    @PostMapping("/save")
    public ModelAndView saveLot(@ModelAttribute LotDto lotDto){


        System.out.println(lotDto);
        return new ModelAndView("redirect:/home");
    }
}
