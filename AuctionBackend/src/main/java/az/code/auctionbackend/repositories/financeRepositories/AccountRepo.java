package az.code.auctionbackend.repositories.financeRepositories;

import az.code.auctionbackend.entities.Account;
import az.code.auctionbackend.entities.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountRepo {
    @Autowired
    private EntityManager em;


    @Transactional
    public Account saveAccount(Account account){
        return em.merge(account);
    }

    public List<Account> getAllAccs(){
        return em.createNativeQuery("SELECT * FROM accounts").getResultList();
    };

    public Optional<Account> searchAccountById(long id){
        return Optional.of((Account) em.createNativeQuery("SELECT * from accounts where id ?1")
                .setParameter(1, id).getSingleResult());
    };
}
