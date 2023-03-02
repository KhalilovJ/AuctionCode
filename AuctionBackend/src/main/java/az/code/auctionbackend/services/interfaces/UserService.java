package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.entities.users.UserProfile;

import java.util.List;

public interface UserService {

    List<UserProfile> getAllProfiles();
}
