package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    public String name;

    public String username;

    public String password;

    public String address;
}
