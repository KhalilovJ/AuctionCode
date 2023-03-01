package az.code.auctionbackend.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@Table(name = "bids")
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="lotId", nullable=false)
    private Lot lot;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private UserProfile user;
}
