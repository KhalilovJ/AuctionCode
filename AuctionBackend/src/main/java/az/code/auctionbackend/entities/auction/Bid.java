package az.code.auctionbackend.entities.auction;


import az.code.auctionbackend.entities.auction.Lot;
import az.code.auctionbackend.entities.users.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@Table(name = "bids")
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="lotId", nullable=false)
    @JsonIgnore
    private Lot lot;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private UserProfile user;

    private double bid;

    private LocalDate bidTime;
}
