package az.code.auctionbackend.DTOs;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class BidDto {

    double bid;
    LocalDate bidTime;
}
