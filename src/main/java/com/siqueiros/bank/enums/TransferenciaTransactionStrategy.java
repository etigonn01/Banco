package com.siqueiros.bank.enums;

import com.siqueiros.bank.exception.AmountIsLessThanOrEqualToZero;
import com.siqueiros.bank.exception.InsufficientFundsException;
import com.siqueiros.bank.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferenciaTransactionStrategy implements TransactionStrategy {
    @Override
    public void execute(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new AmountIsLessThanOrEqualToZero("La cantidad de la transferencia no puede ser cero o un número negativo");
        }

        if (sourceAccount.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("La cuenta no tiene fondos suficientes para transferir");
        }

        if (destinationAccount == null) {
            throw new DataIntegrityViolationException("La cuenta de origen y destino es la misma. Esta petición no está permitida");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
    }
}
