package az.code.auctionbackend.entities;


import az.code.auctionbackend.entities.Bid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@Table(name = "lots")
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

    @Column(name = "current_bid")
    @Nullable
    private double currentBid;

    // TODO Bid history

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "lot")
    private List<Bid> bidHistory;

    @Column(name = "pictures")
    private String itemPictures;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private UserProfile user;

    /*
        0 - not active
        1 - active
        2 - auction finished
        3 - paid
     */
    @Column(name = "status")
    private int status;
}
