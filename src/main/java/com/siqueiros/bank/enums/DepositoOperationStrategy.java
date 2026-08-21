package com.siqueiros.bank.enums;

import com.siqueiros.bank.exception.AmountIsLessThanOrEqualToZero;
import com.siqueiros.bank.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositoOperationStrategy implements OperationStrategy {

    @Override
    public void execute(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new AmountIsLessThanOrEqualToZero("La cantidad de la operación no puede ser cero o un valor negativo");
        }

        account.setBalance(account.getBalance().add(amount));
    }

    @Override
    public boolean supports(String operationName) {
        return "deposito".equalsIgnoreCase(operationName);
    }
}
