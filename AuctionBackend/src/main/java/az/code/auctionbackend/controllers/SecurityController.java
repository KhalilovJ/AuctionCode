package az.code.auctionbackend.controllers;

import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@Slf4j
public class SecurityController {

    @Autowired
    private UserServiceImpl userService;

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


        log.error("start time " + LocalDateTime.now());
        UserProfile userProfile = userService.findByUsername(user.getUsername()).orElse(null);
        log.error("end time " + LocalDateTime.now());

        if (userProfile.getRole().getName().equals("USER")){
            nextPage = new ModelAndView("index");
        } else {
            nextPage = new ModelAndView("adminPanel");
        }

        nextPage.addObject("user", user);
        return nextPage;

//        ModelAndView nextPage = null;
//
//        RedisUser redisUser = redisRepository.getRedisUserByUsername(user.getUsername());
//
//        System.out.println("redisUser " + redisUser);
//
//        if (redisUser.getUsername().equals("USER") ){
//            nextPage = new ModelAndView("index");
//        } else {
//            nextPage = new ModelAndView("adminPanel");
//        }
//
//        nextPage.addObject("user", user);
//        return nextPage;
    }

    @GetMapping("/index")
    public ModelAndView getIndex(@AuthenticationPrincipal UserDetails user){
        ModelAndView nextPage = new ModelAndView("index");

        nextPage.addObject("user", user);
        return nextPage;
    }


}
