package az.code.auctionbackend.repositories.financeRepositories;

import az.code.auctionbackend.entities.Account;
import az.code.auctionbackend.entities.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountRepo {
    @Autowired
    private EntityManager em;


    @Transactional
    public Account saveAccount(Account account){
        return em.merge(account);
    }
}
