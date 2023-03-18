package az.code.auctionbackend.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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


//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name="lotId", nullable=false)
    @ToString.Exclude
    @JsonIgnore
    private Lot lot;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    @JsonIgnore
    private UserProfile user;

    private double bid;

    private LocalDateTime bidTime;
}
