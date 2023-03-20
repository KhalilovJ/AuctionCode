package az.code.auctionbackend.entities.redis;

import az.code.auctionbackend.entities.Bid;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.entities.UserProfile;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("redis")
public class RedisLot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1196524752334552085L;

    @Id
    private Long id;

    private String description;

    private String lotName;

    private double reservePrice;

    private double startingPrice;

    private double bidStep;

    private double currentBid;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private List<Bid> bidHistory;

    private String itemPictures;

    private UserProfile user;

    private UserProfile lotWinner;

    private int status;


//    public void mapLot(Lot lot){
//        id = lot.getId();
//        description = lot.getDescription();
//        reservePrice = lot.getReservePrice();
//        startingPrice = lot.getStartingPrice();
//        bidStep = lot.getBidStep();
////        startDate = lot.getStartDate();
////        endDate = lot.getEndDate();
//
//        bidHistory = new ArrayList<>();
//
//        lot.getBidHistory().forEach(
//                bid -> bidHistory.add(RedisBid.builder()
//                                .id(bid.getId()).lotId(bid.getId()).userId(bid.getUser().getId()).bid(bid.getBid()).bidTime(bid.getBidTime())
//                        .build())
//        );
//    }
}
