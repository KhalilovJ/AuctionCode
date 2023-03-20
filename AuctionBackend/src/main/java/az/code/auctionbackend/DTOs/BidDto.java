package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class BidDto implements Serializable {


    private static final long serialVersionUID = 5537903830430852973L;
    public long lotId;
    public long userId;

    public double bid;
    public LocalDateTime bidTime;
}
