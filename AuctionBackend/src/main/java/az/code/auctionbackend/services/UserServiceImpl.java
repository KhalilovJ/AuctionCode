package az.code.auctionbackend.services;

import az.code.auctionbackend.DTOs.UserDto;
import az.code.auctionbackend.entities.Account;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.UserRepo;
import az.code.auctionbackend.repositories.usersRepositories.RoleRepository;
import az.code.auctionbackend.repositories.usersRepositories.UserRepository;
import az.code.auctionbackend.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRepo userRepo;
    @Autowired
    private RoleRepository roleRepo;

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
                .role(roleRepo.findById(3l).orElse(null)).build();

        Account account = Account.builder().isActive(true).user(user).balance(200).build();

        user.setAccount(account);

        userRepo.saveUser(user);

        log.info(user + " has been saved");

    }

    @Override
    public Optional<UserProfile> findProfileById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserProfile> findByUsername(String username) {
        return  userRepository.findByUsername(username);
    }

    public List<UserProfile> findByIds(List<Long> ids){
        return userRepository.findByIds(ids);
    }
}
