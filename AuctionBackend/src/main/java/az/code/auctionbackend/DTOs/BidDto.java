package az.code.auctionbackend.DTOs;

import az.code.auctionbackend.entities.Bid;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class BidDto implements Serializable {


    private static final long serialVersionUID = 5537903830430852973L;
    private long lotId;
    private long userId;
    private String username;
    private double bid;
    public LocalDateTime bidTime;

    public static BidDto makeBidDto(Bid bid){
        return BidDto.builder()
                .lotId(bid.getLot().getId())
                .userId(bid.getUser().getId())
                .username(bid.getUser().getUsername())
                .bid(bid.getBid())
                .bidTime(bid.getBidTime())
                .build();
    }
}
