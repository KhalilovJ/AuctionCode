package az.code.auctionbackend.DTOs;

import az.code.auctionbackend.entities.Lot;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class LotDto {

    private String description;
    private String lotName;
    private double reservePrice;
    private double startingPrice;
    private double bidStep;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Lot getLot(){
        return Lot.builder()
                .lotName(lotName)
                .description(description).reservePrice(reservePrice).startingPrice(startingPrice)
                .bidStep(bidStep).startDate(startDate).endDate(endDate)
        .build();
    }
}
