package az.code.auctionbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Entity
@Data
@Builder
@Table(name = "profiles")
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

    private double rating;

    @Value("false")
    private boolean isBlocked;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private Account account;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="roleId", nullable=false)
    @ToString.Exclude
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Bid> bidList;

    public String toString(){
        return "User (id: " + id +
                " Username: " + username +
                " Name: " + name +
                " Address: " + address +
                " Rating: " + rating + " )";
    }

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<Transaction> transactionList;
}
