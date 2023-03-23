package az.code.auctionbackend.entities;


import az.code.auctionbackend.entities.Bid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "id", nullable = false)
//    private Long id;

    @Column
    private String description;
    @Column
    private String lotName;

    @Column(name = "reserve_price")
    private double reservePrice;

    @Column(name = "starting_price")
    private double startingPrice;

    @Column(name = "bid_step")
    private double bidStep;

    @Column(name = "current_bid")
    @Nullable
    private double currentBid;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "lot", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bid> bidHistory;

    @Column(name = "pictures")
    private String itemPictures;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private UserProfile user;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "winnerId")
//    @JsonIgnore
    private UserProfile lotWinner;

    /**

       -2 - not sold
       -1 - not approved
        0 - not active
        1 - active
        2 - auction finished
        3 - waiting to approve by second place
        4 - paid

     */
    @Column(name = "status")
    private int status;
}
