package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.users.UserProfile;
import az.code.auctionbackend.repositories.UserRepository;
import az.code.auctionbackend.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public List<UserProfile> getAllProfiles() {
        return userRepository.findAll();
    }
}
