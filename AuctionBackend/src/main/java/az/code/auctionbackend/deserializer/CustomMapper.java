package az.code.auctionbackend.deserializer;

import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.entities.redis.RedisUser;
import az.code.auctionbackend.repositories.usersRepositories.RoleRepository;
import az.code.auctionbackend.repositories.usersRepositories.UserRepository;
import az.code.auctionbackend.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Component
@Slf4j
@AllArgsConstructor
public class CustomMapper {

    private final RoleRepository roleRepository;

    public RedisUser mapperUserProfileToRedisUser(UserProfile userProfile) {

        RedisUser redisUser = RedisUser.builder()
                .address(userProfile.getAddress())
                .name(userProfile.getName())
                .password(userProfile.getPassword())
                .username(userProfile.getUsername())
                .id(userProfile.getId())
                .rating(userProfile.getRating())
                .role(userProfile.getRole().getName())
                .build();

        log.info("mapperUserProfileToRedisUser \n" + redisUser);

        return redisUser;
    }

    public  UserProfile mapperRedisUserToUserProfile(RedisUser redisUser) {

        long userId = redisUser.getId();

        UserProfile userProfile = UserProfile.builder()
                .id(userId)
                .name(redisUser.getName())
                .username(redisUser.getUsername())
                .password(redisUser.getPassword())
                .address(redisUser.getAddress())
                .rating(redisUser.getRating())
                .role(roleRepository.findRoleByName(redisUser.getRole()).get())
                .build();

        log.info("mapperRedisUserToUserProfile \n" + userProfile);

        return userProfile;
    }

}
