package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.deserializer.CustomMapper;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class FrontController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomMapper mapper;

    @GetMapping("/")
    public ModelAndView getIndex(){
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/lots/{lotId}")
    public ModelAndView getLot(@PathVariable Long lotId){

        ModelAndView model;

        RedisLot redisLot = redisRepository.getRedis(lotId);
        log.error("getLot " + redisLot);
        Lot lot = objectMapper.convertValue(redisLot, Lot.class);

        // TODO status change
        // closed lot
        if (lot == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        log.info("lot " + lot);
        log.info("redisRepository.getRedisUser(redisLot.getUserId()) "
                + redisRepository.getRedisUser(redisLot.getUserId()));

        lot.setUser(mapper.mapperRedisUserToUserProfile(
                redisRepository.getRedisUser(redisLot.getUserId())));

        log.info("lot.getUser() " + lot.getUser());

        /**
         *  Создание пустого списка бидов и заполноение в противном случае
         */
        if (redisLot.getBidHistory() == null) {
            lot.setBidHistory(new ArrayList<>());
        } else {
            List<Bid> bidList = new ArrayList<>();

            for (BidDto bidDto : redisLot.getBidHistory()) {
                bidList.add(Bid.builder()
                        .bidTime(bidDto.getBidTime())
                        .lot(lot)
                        .bid(bidDto.getBid())
                        .user(mapper.mapperRedisUserToUserProfile(redisRepository.getRedisUser(bidDto.getUserId())))
                        .build());
            }
            lot.setBidHistory(bidList);
        }

//        lot.setUser(userService.findProfileById(redisLot.getUserId()).get());
//        System.out.println("from db " + lot);

        log.info("lot " + lot);

//        Lot lot = realtimeRepo.getLot(lotId);

//        if (lot == null){
//
//            lot = objectMapper.convertValue(redisRepository.getRedis(lotId), Lot.class);
//
////            lot = lotService.findRedisLotByIdActive(lotId);
//            lot.setCurrentBid(lot.getStartingPrice());
//            realtimeRepo.addLot(lot);
//        }

       // TODO для if что-то еще надо делать?
        if (lot.getStatus() < 0){
            model = new ModelAndView("index");
        } else {
            model = new ModelAndView("auction");
            model.addObject("auction", lot);
            model.addObject("user", lot.getUser());
        }

        return model;
    }

    @GetMapping("/user/{username}/add_auction")
    public ModelAndView newAuction(@PathVariable String username, @AuthenticationPrincipal UserDetails user){

        String un = user.getUsername();

        if (username.equalsIgnoreCase(un)) {

            // TODO validation
            // userService.findSellerProfileById(un).isChecked() - verified seller
            if (!userService.findSellerProfileById(un).isChecked()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ModelAndView model = new ModelAndView("newAuction");
            LotDto lotDto = LotDto.builder().build();
            model.addObject("lot", lotDto);

            log.info("newAuction " + lotDto);

            return model;
        } else {
            // if username is incorrect
            return new ModelAndView("redirect:/home");
        }
    }

}
