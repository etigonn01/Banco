package com.siqueiros.bank.exception;

public class AccountWithBalanceException extends RuntimeException {
    public AccountWithBalanceException(String message) {
        super(message);
    }

    public static AccountWithBalanceException of(long accounId) {
        return new AccountWithBalanceException(
                String.format("La cuenta con Id '%s' aún cuenta con saldo, retire el dinero para eliminarla.", accounId)
        );
    }
}
