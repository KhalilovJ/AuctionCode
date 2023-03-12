package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class BidDto {

    public long lotId;
    public long userId;

    public double bid;
    public LocalDateTime bidTime;
}
