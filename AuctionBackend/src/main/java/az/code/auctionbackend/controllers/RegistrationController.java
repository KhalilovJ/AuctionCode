package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Log4j2
@RequiredArgsConstructor
public class RegistrationController {

    private final UserServiceImpl userService;

    @GetMapping("/registration")
    public ModelAndView registration(Model model){
        ModelAndView nextPage = new ModelAndView("registration");

        model.addAttribute("user", UserDto.builder().build());
        return nextPage;
    }
    @PostMapping("/register")
    public String register(@Valid UserDto user, BindingResult result, Model model){

        log.info("User is " + user);


        if (result.hasErrors() || userService.checkUser(user.getUsername()) != null) {

            model.addAttribute("user", UserDto.builder().build());

            return "registration";
        } else {
            userService.createUser(user);
            return "login";
        }

    }



    private boolean validateString(String string){
        Pattern pattern = Pattern.compile("[A-Za-z_]");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
