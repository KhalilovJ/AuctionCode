package az.code.auctionbackend.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityController {

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/index")
    public ModelAndView getHome(@AuthenticationPrincipal UserDetails user){
        ModelAndView nextPage = new ModelAndView("index");

        nextPage.addObject("user", user);
        return nextPage;
    }
}
