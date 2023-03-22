package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.Account;
import az.code.auctionbackend.entities.Transaction;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.financeRepositories.TransactionRepository;
import az.code.auctionbackend.services.interfaces.AccountService;
import az.code.auctionbackend.services.interfaces.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Access;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class TranactionService {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    EntityManager entityManager;

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
//                .senderAccountId(users.get(0).getAccount().getId())
                .transactionTime(LocalDateTime.now())
//                .senderAccount(users.get(0).getAccount())
                .build();

        log.info(transactionRepository.save(transaction));

        return transaction;
    }

    public Transaction saveTransaction(Transaction transaction){
//        return transactionRepository.save(transaction);
        return entityManager.merge(transaction);
    }

    @Transactional
    public Account saveAccount(Account acc){
        return entityManager.merge(acc);
    }

}
