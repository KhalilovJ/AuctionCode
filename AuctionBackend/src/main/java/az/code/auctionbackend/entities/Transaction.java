package az.code.auctionbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder(toBuilder = true)
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
    @ToString.Exclude
    private Account account;
//
////    @ManyToOne
////    @JoinColumn(name = "senderId", nullable = false)
////    UserProfile sender;
//
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId",insertable=false, updatable=false)
    @ToString.Exclude
    private Account senderAccount;

    @Column(name = "senderAccId")
    Long senderAccountId;

    @Column(name = "time")
    LocalDateTime transactionTime;
}
