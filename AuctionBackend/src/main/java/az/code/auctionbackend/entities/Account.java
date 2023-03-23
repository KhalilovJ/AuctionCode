package az.code.auctionbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;


@Entity
@Getter
@Setter
@Builder
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    @ToString.Exclude
    private UserProfile user;

    private double balance;


    // for user banning

    @Value("true")
    private boolean isActive;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactions;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "senderAccount", cascade = CascadeType.ALL)
//    @JsonIgnore
//    @ToString.Exclude
//    private List<Transaction> transactionsSent;


}
