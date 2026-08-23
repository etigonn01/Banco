package com.siqueiros.bank.exception;

public class DuplicatedAccountException extends RuntimeException {
    public DuplicatedAccountException(String message) {
        super(message);
    }

    public static DuplicatedAccountException of(long id) {
        return new DuplicatedAccountException(
                String.format("El usuario con Id: %d ya tiene registrada una cuenta de este tipo", id)
        );
    }
}
