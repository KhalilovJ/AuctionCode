package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Data
@ToString
public class BidResponseDto {

    private long id;

    private double lotCurrentBidPrice;

    private long lotId;
    private long userId;

    private String username;

    private double bid;
    private LocalDateTime bidTime;
}
