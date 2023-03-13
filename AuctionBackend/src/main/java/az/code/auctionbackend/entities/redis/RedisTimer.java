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
public class RedisTimer implements Serializable {

    @Serial
    private static final long serialVersionUID = 8820922885878929357L;

    private long id;

    private LocalDateTime endDate;
}
