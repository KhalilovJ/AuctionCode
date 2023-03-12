package az.code.auctionbackend.entities.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("redis")
public class RedisBid implements Serializable {

    @Serial
    private static final long serialVersionUID = 1196524752334552085L;
    private Long id;
    private Long lotId;
    private Long userId;

    private double bid;

    private LocalDateTime bidTime;
}
