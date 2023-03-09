package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BidDto {

    public long lotId;
    public long userId;
    public double bid;
    public LocalDate bidTime;
}
