package az.code.auctionbackend.entities.finance;

import az.code.auctionbackend.entities.users.UserProfile;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class Transaction {

//    @ManyToOne
//    @JoinColumn
//    Account account;

    @ManyToOne
    UserProfile sender;
}
