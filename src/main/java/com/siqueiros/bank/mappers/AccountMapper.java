package com.siqueiros.bank.mappers;

import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountResponseDTO toResponse (Account account) {
        if (account == null) return null;

        return new AccountResponseDTO(
                account.getId(),
                account.getBalance(),
                this.extractTypeAccountName(account),
                this.extractUserFullName(account),
                account.getCreatedAt()
        );
    }

    private String extractTypeAccountName(Account account) {
        return (account.getTypeAccount() != null) ? account.getTypeAccount().getName() : null;
    }

    private String extractUserFullName(Account account) {
        return (account.getUser() != null) ? account.getUser().getFullName() : null;
    }
}
