package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.entities.users.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserProfile> getAllProfiles();

    Optional<UserProfile> findProfileById(long id);

    Optional<UserProfile> findByUsername(String username);
}
