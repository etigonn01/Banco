package com.siqueiros.bank.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }

    public static AccountAlreadyExistsException of(long id) {
        return new AccountAlreadyExistsException(
                String.format("El usuario con Id: %d ya tiene registrada una cuenta de este tipo", id)
        );
    }
}
