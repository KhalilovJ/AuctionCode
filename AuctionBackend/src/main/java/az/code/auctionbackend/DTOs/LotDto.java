package az.code.auctionbackend.DTOs;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class LotDto {

    private String description;
    private double reservePrice;
    private double startingPrice;
    private double bidStep;

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
