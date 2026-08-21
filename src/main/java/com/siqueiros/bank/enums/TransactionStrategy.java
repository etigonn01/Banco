package com.siqueiros.bank.enums;

import com.siqueiros.bank.model.Account;

import java.math.BigDecimal;

public interface TransactionStrategy {
    void execute(Account sourceAccount, Account destinationAccount, BigDecimal amount);
}
