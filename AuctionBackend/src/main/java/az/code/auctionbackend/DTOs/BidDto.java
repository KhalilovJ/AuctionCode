package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BidDto {

    double bid;
    LocalDate bidTime;
}
