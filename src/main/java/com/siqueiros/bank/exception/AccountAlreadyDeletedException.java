package com.siqueiros.bank.exception;

public class AccountAlreadyDeletedException extends RuntimeException {
    public AccountAlreadyDeletedException(String message) {
        super(message);
    }

    public static AccountAlreadyDeletedException of(long accountId) {
        return new AccountAlreadyDeletedException(
                String.format("La cuenta con Id '%s' ya se encuentra inactiva.", accountId)
        );
    }
}
