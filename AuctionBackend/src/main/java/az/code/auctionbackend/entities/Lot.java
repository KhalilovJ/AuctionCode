package az.code.auctionbackend.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "lots")
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String description;

    @Column(name = "reserve_price")
    private double reservePrice;

    @Column(name = "starting_price")
    private double startingPrice;

    @Column(name = "bid_step")
    private double bidStep;

    // TODO Bid history

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "lot")
    private List<Bid> bidHistory;
}
