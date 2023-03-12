package az.code.auctionbackend.repositories.financeRepositories;

import az.code.auctionbackend.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
