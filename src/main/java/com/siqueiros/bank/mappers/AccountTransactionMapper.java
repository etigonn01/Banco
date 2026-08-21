package com.siqueiros.bank.mappers;

import com.siqueiros.bank.dto.AccountTransactionResponseDTO;
import com.siqueiros.bank.model.AccountTransaction;
import org.springframework.stereotype.Component;

@Component
public class AccountTransactionMapper {
    public AccountTransactionResponseDTO toResponseDTO(AccountTransaction accountTransaction) {
        return new  AccountTransactionResponseDTO(
                accountTransaction.getId(),
                accountTransaction.getTypeOperationName(),
                accountTransaction.getUserFullName(),
                accountTransaction.getAmount(),
                accountTransaction.getTransactionDateTime()
        );
    }
}
