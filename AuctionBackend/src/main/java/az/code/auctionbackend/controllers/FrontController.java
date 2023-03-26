package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.BidDto;
import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.DTOs.LotFrontDto;
import az.code.auctionbackend.DTOs.UserFrontDTO;
import az.code.auctionbackend.deserializer.CustomMapper;
import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisLot;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.LotServiceImpl;
import az.code.auctionbackend.services.interfaces.LotService;
import az.code.auctionbackend.services.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
public class FrontController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private LotService lotService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomMapper mapper;

    @GetMapping("/")
    public ModelAndView getIndex(){
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal UserDetails userIn){
        ModelAndView model = new ModelAndView("profile");
        UserFrontDTO user = userService.getUserToFront(userIn.getUsername());
        model.addObject("user", user);
        return model;
    }

    @GetMapping("/lots/{lotId}")
    public ModelAndView getLot(@PathVariable Long lotId){

        ModelAndView model;
        LotFrontDto lotFront = null;

        RedisLot redisLot = redisRepository.getRedis(lotId);

        if (redisLot != null) {
            log.error("getLot " + redisLot);
            lotFront = LotFrontDto.getLotFrontDto(redisLot);
            UserFrontDTO userFrontDTO = UserFrontDTO.convertToUserFront(userService.findProfileById(lotFront.getUserId()).get());
            lotFront.setUser(userFrontDTO);

            /**
             *  Создание пустого списка бидов
             */
            if (lotFront.getBids() == null) {
                lotFront.setBids(new ArrayList<>());
            }

        } else { //redislot is null
            Lot lot = lotService.findLotById(lotId).orElse(null);
            if (lot != null){
                lotFront = LotFrontDto.getLotFrontDto(lot);
            } else { // there's no lot with this id
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        log.info("lot " + lotFront);

        if (lotFront.getStatus() == -1){
            model = new ModelAndView("redirect:/home");
            return model;
        }
        else {
            model = new ModelAndView("auction");
            Collections.reverse(lotFront.getBids());
            model.addObject("auction", lotFront);
            model.addObject("user", lotFront.getUser());
        }

        return model;
    }

    @GetMapping("/user/{username}/add_auction")
    public ModelAndView newAuction(@PathVariable String username, @AuthenticationPrincipal UserDetails user){

        String un = user.getUsername();

        if (username.equalsIgnoreCase(un)) {

            UserProfile userProfile = userService.findByUsername(username).get();

            // TODO validation
            // userService.findSellerProfileById(un).isChecked() - verified seller
            if (!userProfile.isSellerActive()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
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
