package az.code.auctionbackend.entities.redis;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("redis")
public class RedisUser implements Serializable {

    private static final long serialVersionUID = 6703811972517405461L;

    @Id
    private long id;

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

    private double rating;
}
