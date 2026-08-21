package com.siqueiros.bank.enums;

import com.siqueiros.bank.exception.AmountIsLessThanOrEqualToZero;
import com.siqueiros.bank.exception.InsufficientFundsException;
import com.siqueiros.bank.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RetiroOperationStrategy implements OperationStrategy {
    @Override
    public void execute(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new AmountIsLessThanOrEqualToZero("La cantidad de la operación no puede ser cero o un valor negativo");
        }

        if (account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("La cuenta no tiene el saldo para realizar la operación");
        }
        account.setBalance(account.getBalance().subtract(amount));
    }

    @Override
    public boolean supports(String operationName) {
        return "retiro".equalsIgnoreCase(operationName);
    }
}
