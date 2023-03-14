package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.Account;
import az.code.auctionbackend.entities.Transaction;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.financeRepositories.TransactionRepository;
import az.code.auctionbackend.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TranactionService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TransactionRepository transactionRepository;


    public Transaction createTransaction(double amount, long senderId, long receiverId){

        List<Long> ids = new ArrayList<>();
        ids.add(senderId);
        ids.add(receiverId);

        List<UserProfile> users = userService.findByIds(ids);

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .account(users.get(1).getAccount())
                .sender(users.get(0))
                .build();

        transactionRepository.save(transaction);

        return transaction;
    }

}
