package com.siqueiros.bank.mappers;

import com.siqueiros.bank.dto.TransactionResponseDTO;
import com.siqueiros.bank.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponseDTO toResponseDTO(Transaction transaction) {
        return  new TransactionResponseDTO(
                transaction.getId(),
                transaction.getSourceAccountFullName(),
                transaction.getDestinationAccountFullName(),
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }
}
