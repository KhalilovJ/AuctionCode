package az.code.auctionbackend.services.interfaces;

import az.code.auctionbackend.entities.finance.Account;
import az.code.auctionbackend.entities.finance.Transaction;
import az.code.auctionbackend.entities.users.UserProfile;

import java.util.Optional;

public interface AccountService {

    double getBalance(long accountId);

    Account getAccountDetails(long accountId);

    void topUpBalance(long accountId, double amount);

    Transaction purchase(UserProfile sender, UserProfile receiver, double amount);
}