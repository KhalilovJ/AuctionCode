package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.users.UserProfile;
import az.code.auctionbackend.repositories.RoleRepository;
import az.code.auctionbackend.repositories.UserRepo;
import az.code.auctionbackend.repositories.UserRepository;
import az.code.auctionbackend.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRepo userRepo;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UserProfile> getAllProfiles() {
        return userRepository.findAll();
    }


    public UserProfile checkUser(String username){
        UserProfile user = userRepository.findByUsername(username).orElse(null);
        if (user != null){
            log.info(user.getUsername() + " found; id: " + user.getId());
        } else {
            log.info("User " + username + " not found");
        }
        return user;
    }

    public void createUser(UserDto userDto){
        UserProfile user = UserProfile.builder().username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .address(userDto.getAddress())
                .isBlocked(false)
                .rating(5)
                .role(roleRepository.findById(3l).orElse(null)).build();

        userRepo.saveUser(user);

        log.info(user + " has been saved");

    }

}
