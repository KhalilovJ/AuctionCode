package az.code.auctionbackend.entities;


import jakarta.persistence.*;
import lombok.*;

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
}
