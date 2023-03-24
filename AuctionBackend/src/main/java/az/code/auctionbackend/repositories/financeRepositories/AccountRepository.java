package az.code.auctionbackend.repositories.financeRepositories;

import az.code.auctionbackend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.id = ?1")
    Optional<Account> getAccountBy(long accountId);

//    @Query(value = "SELECT * FROM accounts", nativeQuery = true)
//    Optional<List<Account>> getAllAccs();
    Optional<Account> getAccountById(long accountId);
}
