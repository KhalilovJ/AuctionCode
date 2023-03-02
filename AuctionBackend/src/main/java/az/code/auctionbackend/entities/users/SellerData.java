package az.code.auctionbackend.entities.users;

import az.code.auctionbackend.entities.auction.Bid;
import az.code.auctionbackend.entities.finance.Account;
import az.code.auctionbackend.entities.users.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@Table(name = "sellers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private long TIN;
    private boolean checked;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserProfile userProfile;
}
