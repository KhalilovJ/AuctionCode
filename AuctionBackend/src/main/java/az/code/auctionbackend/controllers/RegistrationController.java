package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisUser;
import az.code.auctionbackend.repositories.redisRepositories.RedisRepository;
import az.code.auctionbackend.services.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@Log4j2
@RequiredArgsConstructor
public class RegistrationController {

    private final UserServiceImpl userService;
    private final RedisRepository redisRepository;

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
            UserProfile userProfile = userService.createUser(user);

            log.info("New userProfile: " + userProfile);

            redisRepository.saveRedisUser(RedisUser.builder()
                    .id(userProfile.getId())
                    .name(userProfile.getName())
                    .username(userProfile.getUsername())
                    .password(userProfile.getPassword())
                    .address(userProfile.getAddress())
                    .rating(userProfile.getRating())
                    .role(userProfile.getRole().getName())
                    .build());

            return "login";
        }

    }

}
