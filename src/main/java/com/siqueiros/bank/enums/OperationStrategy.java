package com.siqueiros.bank.enums;

import com.siqueiros.bank.model.Account;

import java.math.BigDecimal;

public interface OperationStrategy {
    void execute(Account account, BigDecimal amount);
    boolean supports(String operationName);
}
