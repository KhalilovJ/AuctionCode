package az.code.auctionbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


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

    // TODO temporarily
    @Value("true")
    private boolean checked;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserProfile userProfile;
}
