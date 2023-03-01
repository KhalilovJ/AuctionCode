package az.code.auctionbackend.controllers;

import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.services.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    //objectMapper.convertValue(userService.getAllProfiles(), UserDto.class)
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllProfiles() {

        return ResponseEntity.ok(userService.getAllProfiles().stream()
                .map(x -> objectMapper.convertValue(x, UserDto.class))
                .collect(Collectors.toList()));
    }
}
