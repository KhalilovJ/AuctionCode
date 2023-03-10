package az.code.auctionbackend.controllers;

import az.code.auctionbackend.entities.users.UserProfile;
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

    @GetMapping("/login")
    public ModelAndView login(@AuthenticationPrincipal UserDetails user) {
        ModelAndView nextPage;
        if (user != null){
            nextPage = new ModelAndView("index");
            nextPage.addObject("user", user);
        } else {
            nextPage = new ModelAndView("login");
        }

        return nextPage;
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
