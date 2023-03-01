package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.entities.UserProfile;

import java.util.List;

public interface UserService {

    List<UserProfile> getAllProfiles();
}
