package az.code.auctionbackend.entities.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("waitings")
public class RedisWaitingPayment implements Serializable {
    private static final long serialVersionUID = -6615161855664772725L;

    @Id
    private long id;
    private long senderId;
    private long receiverId;
    private double amount;
    private LocalDateTime creationTime;

}
