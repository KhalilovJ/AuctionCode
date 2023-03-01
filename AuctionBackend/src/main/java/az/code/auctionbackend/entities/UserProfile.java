package az.code.auctionbackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@Table(name = "profiles")
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String username;

    private String password;

    private String address;

    private Double rating;

}
