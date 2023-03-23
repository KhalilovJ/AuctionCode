package az.code.auctionbackend.services;

import az.code.auctionbackend.entities.Account;
import az.code.auctionbackend.entities.Transaction;
import az.code.auctionbackend.entities.UserProfile;
import az.code.auctionbackend.repositories.UserRepo;
import az.code.auctionbackend.repositories.financeRepositories.AccountRepo;
import az.code.auctionbackend.repositories.financeRepositories.AccountRepository;
import az.code.auctionbackend.repositories.financeRepositories.TransactionRepository;
import az.code.auctionbackend.repositories.usersRepositories.UserRepository;
import az.code.auctionbackend.services.interfaces.AccountService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UserRepo userRepo;
    private final AccountRepo accRepo;

    private final TranactionService tranactionService;

//    @PostConstruct
    public void AccountTest() {

        //Error
//        transactionRepository.save(Transaction.builder()
//                .account(getAccountDetails(1))
//                .amount(5)
//                .sender(getAccountDetails(2).getUser()).build());

//        System.out.println(getAccountDetails(1));
//        System.out.println(getAccountDetails(3));

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
    public List<Account> getAllAccounts() {
        return accRepo.getAllAccs();
//        return  accountRepository.getAllAccs().get();
    }

    @Override
    public Account getAccountDetails(long accountId) {

//        return accountRepository.getAccountById(accountId).orElse(null);
        return accRepo.searchAccountById(accountId).orElse(null);
    }


    @Override
    public double getBalance(long accountId) {

        return accRepo.searchAccountById(accountId)
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

//        accountRepository.save(account);0.
        log.info("saving account " + account);
        log.info("saved" + accRepo.saveAccount(account));
    }

    @Override
    public Transaction purchase(long senderId, long receiverId, double amount) {

//        Account senderAccount = accountRepository.getAccountById(senderId).orElse(null);
//        Account receiverAccount = accountRepository.getAccountById(receiverId).orElse(null);
        Account senderAccount = accRepo.searchAccountById(senderId).orElse(null);
        Account receiverAccount = accRepo.searchAccountById(receiverId).orElse(null);

        // TODO validation
        if(!senderAccount.isActive() || !receiverAccount.isActive()) {
            // userService.findByUsername(un).get().isBlocked()
            return null;
        }

        // TODO validation
        if (senderAccount.getBalance() < amount) {
            // status - 406
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

//        Transaction tr =  makePurchase(sender, receiver, amount);
//        return tr;
        topUpBalance(senderId, amount * -1);
//
        topUpBalance(receiverId, amount);

        return tranactionService.createTransaction(amount, receiverId, senderId);

    }



//    private Transaction makePurchase(UserProfile sender, UserProfile receiver, double amount){
//
//        Transaction transaction = Transaction.builder()
//                .amount(amount)
//                .transactionTime(LocalDateTime.now())
//                .build();
//
//        Account senderAccount = topUpBalance(sender, amount);
//        Account receiverAccount = getAccountDetails(receiver.getAccount().getId());
//
//        senderAccount.setBalance(senderAccount.getBalance() - amount);
//        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
//
//        transaction = transaction.toBuilder()
//                .senderAccount(senderAccount)
//                .senderAccountId(senderAccount.getId())
//                .account(receiverAccount).build();
//
//        if (senderAccount.getTransactions() == null){
//            List<Transaction> newList = new ArrayList<>();
//            newList.add(transaction);
//            senderAccount.setTransactions(newList);
//        } else {
//            senderAccount.getTransactions().add(transaction);
//        }
//
//        Account acc =tranactionService.saveAccount(receiverAccount);
//
//        System.out.println("saved " + acc);
//        return transaction;
//
//    }
}