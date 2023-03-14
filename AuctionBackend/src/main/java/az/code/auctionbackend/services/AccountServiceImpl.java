package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.Account;
import az.code.auctionbackend.entities.Transaction;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.financeRepositories.AccountRepository;
import az.code.auctionbackend.repositories.financeRepositories.TransactionRepository;
import az.code.auctionbackend.repositories.usersRepositories.UserRepository;
import az.code.auctionbackend.services.interfaces.AccountService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    private final TranactionService tranactionService;


//    @PostConstruct
    public void AccountTest() {

        //Error
//        transactionRepository.save(Transaction.builder()
//                .account(getAccountDetails(1))
//                .amount(5)
//                .sender(getAccountDetails(2).getUser()).build());

        System.out.println(getAccountDetails(1));
        System.out.println(getAccountDetails(2));

//        topUpBalance(1, -10);
//        topUpBalance(2, 100);
//        List<Transaction> transactions = userRepository.findById(2L).get().getAccount().getTransactions();
//
//
//        System.out.println(purchase(userRepository.findById(2L).get(), userRepository.findById(3L).get(), 1));
//        System.out.println(getAccountDetails(1));
//        System.out.println(getAccountDetails(2));
    }


    @Override
    public Account getAccountDetails(long accountId) {
        return accountRepository.getAccountBy(accountId).orElse(null);
    }

    @Override
    public double getBalance(long accountId) {

        return accountRepository.getAccountBy(accountId)
                .map(Account::getBalance)
                .orElse(0.0);
    }

    @Override
    public void topUpBalance(long accountId, double amount) {

        Account account = getAccountDetails(accountId);
        // TODO validation
        if (account == null || !account.isActive()) return;

        double balance = account.getBalance();
        account.setBalance(balance + amount);

        accountRepository.save(account);
    }

    @Override
    @Transactional
    public Transaction purchase(UserProfile sender, UserProfile receiver, double amount) {

        Account senderAccount = sender.getAccount();
        Account receiverAccount = receiver.getAccount();
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .account(receiverAccount)
                .sender(sender)
                .build();

        //TODO validation
        if(!senderAccount.isActive() || !receiverAccount.isActive()) {
            return null;
        }
        //TODO validation
        if (senderAccount.getBalance() < amount) {
            return null;
        }
        // update sender`s Balance
        topUpBalance(senderAccount.getId(), amount * -1);
//        addTransaction(senderAccount, transaction);

        // TODO fix bug. transaction

        // update receiver`s Balance
        topUpBalance(receiverAccount.getId(), amount);
//        addTransaction(receiverAccount, transaction);

        return transaction;
    }


    @Transactional
    public Transaction purchaseV2(UserProfile sender, UserProfile receiver, double amount){

        topUpBalance(sender.getAccount().getId(), amount*-1);

        topUpBalance(receiver.getAccount().getId(), amount);

        return tranactionService.createTransaction(amount, receiver.getId(), sender.getId());

    }


}