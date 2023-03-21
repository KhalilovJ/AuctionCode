package az.code.auctionbackend.controllers;

import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
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

        UserProfile userProfile = userService.findByUsername(user.getUsername()).orElse(null);

        if (userProfile.getRole().getName().equals("USER") ){
            nextPage = new ModelAndView("index");
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
