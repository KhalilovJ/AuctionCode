package az.code.auctionbackend.entities.redis;

import az.code.auctionbackend.entities.Lot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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

    private Long id;

    private String description;

    private double reservePrice;

    private double startingPrice;

    private double bidStep;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<RedisBid> bidHistory;


    public void mapLot(Lot lot){
        id = lot.getId();
        description = lot.getDescription();
        reservePrice = lot.getReservePrice();
        startingPrice = lot.getStartingPrice();
        bidStep = lot.getBidStep();
//        startDate = lot.getStartDate();
//        endDate = lot.getEndDate();

        bidHistory = new ArrayList<>();

        lot.getBidHistory().forEach(
                bid -> bidHistory.add(RedisBid.builder()
                                .id(bid.getId()).lotId(bid.getId()).userId(bid.getUser().getId()).bid(bid.getBid()).bidTime(bid.getBidTime())
                        .build())
        );
    }
}
