package az.code.auctionbackend.DTOs;

import az.code.auctionbackend.entities.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserFrontDTO {

    private Long id;
    private String name;
    private String username;
    private String address;
    private double rating;
    private double balance;
    private int wonLotsCount;
    private int soldLotsCount;
    private String email;
    private String phoneNumber;
    private String zipCode;

    public static UserFrontDTO convertToUserFront(UserProfile userProfile){
        int soldLotsCount = userProfile.getLots().stream().filter(a->{
            if (a.getStatus() == 3) {
                return true;
            } else {
                return false;
            }
        }).toList().size();
        return UserFrontDTO.builder()
                .id(userProfile.getId())
                .name(userProfile.getName())
                .username(userProfile.getUsername())
                .address(userProfile.getAddress())
                .rating(userProfile.getRating())
                .balance(userProfile.getAccount().getBalance())
                .wonLotsCount(userProfile.getWonLots().size())
                .soldLotsCount(soldLotsCount)
                .email(userProfile.getEmail())
                .phoneNumber(userProfile.getPhoneNumber())
                .zipCode(userProfile.getZipCode())
                .build();
    }
}
