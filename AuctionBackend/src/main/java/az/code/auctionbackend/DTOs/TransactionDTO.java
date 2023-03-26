package az.code.auctionbackend.DTOs;

import az.code.auctionbackend.entities.Transaction;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDTO {
    private double amount;
    private String fromUsername;
    private String toUsername;
    private LocalDateTime time;
    private int status;

    public static TransactionDTO getTransactionDto(Transaction tr){
        return TransactionDTO.builder()
                .amount(tr.getAmount())
                .fromUsername(tr.getSenderUsername())
                .toUsername(tr.getAccount().getUser().getUsername())
                .time(tr.getTransactionTime())
                .status(tr.getStatus())
                .build();
    }
}
