package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String name;

    private String username;

    private String password;

    private String address;
}
