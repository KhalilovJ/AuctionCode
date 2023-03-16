package az.code.auctionbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "amount")
    private double amount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "senderId", nullable = false)
    UserProfile sender;
}
