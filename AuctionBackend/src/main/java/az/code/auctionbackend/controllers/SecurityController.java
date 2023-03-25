package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.LotFrontDto;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.UserServiceImpl;
import az.code.auctionbackend.services.interfaces.LotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class SecurityController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private LotService lotService;
    @Autowired
    private RedisRepository redisRepository;

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails user) {
        if (user != null){
            return "redirect:/home";
        } else {
            return "login";
        }
    }

    @GetMapping("/home")
    public ModelAndView getHome(@AuthenticationPrincipal UserDetails user){

        ModelAndView nextPage = null;
        UserProfile userProfile = userService.findByUsername(user.getUsername()).orElse(null);

        if (userProfile.getRole().getName().equals("USER")){

            List<LotFrontDto> lotList = lotService.getAllActiveLotsFront();
            nextPage = new ModelAndView("index");
            nextPage.addObject("lotList", lotList);
        } else {
            nextPage = new ModelAndView("adminPanel");
        }

        nextPage.addObject("user", user);
        return nextPage;

    }

    @GetMapping("/index")
    public ModelAndView getIndex(@AuthenticationPrincipal UserDetails user){
        ModelAndView nextPage = new ModelAndView("index");

        nextPage.addObject("user", user);
        return nextPage;
    }


}
