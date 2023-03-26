package az.code.auctionbackend.entities.redis;

import az.code.auctionbackend.DTOs.BidDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
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

    private List<BidDto> bidHistory;

//    private List<Long> bidHistory;

    private String itemPictures;

    private long userId;

    private long lotWinnerId;

    private int status;
    private String imgs;


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
