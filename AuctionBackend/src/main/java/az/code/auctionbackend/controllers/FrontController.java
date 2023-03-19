package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.LotDto;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.auctionRepositories.AuctionRealtimeRepo;
import az.code.auctionbackend.services.LotServiceImpl;
import az.code.auctionbackend.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
public class FrontController {

    @Autowired
    private LotServiceImpl lotService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuctionRealtimeRepo realtimeRepo;

    @GetMapping("/")
    public ModelAndView getIndex(){
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/lots/{lotId}")
    public ModelAndView getLot(@PathVariable Long lotId){

        ModelAndView model;

        Lot lot = realtimeRepo.getLot(lotId);

        if (lot == null){lot = lotService.findRedisLotByIdActive(lotId);
            lot.setCurrentBid(lot.getStartingPrice());
            realtimeRepo.addLot(lot);
        }

        if (lot == null || lot.getStartDate() != null && lot.getStartDate().isAfter(LocalDateTime.now())){
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
        if (username.equalsIgnoreCase(user.getUsername())){
        ModelAndView model = new ModelAndView("newAuction");
        LotDto lotDto = LotDto.builder().build();
        model.addObject("lot", lotDto);
        return model;}


        else { // if username is incorrect
            return new ModelAndView("redirect:/home");
        }
    }

}
