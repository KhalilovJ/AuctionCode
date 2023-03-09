package az.code.auctionbackend.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@ToString
public class UserDto {

    @NotEmpty
    @NotNull
    @Size(min = 3)
    private String name;

    @NotEmpty
    @NotNull
    @Size(min = 3)
    private String username;

    @NotEmpty
    @NotNull
    @ToString.Exclude
    @Size(min = 5)
    private String password;

    @NotEmpty
    private String address;

}
