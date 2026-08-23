package com.siqueiros.bank.exception;

public class DuplicatedAccountException extends RuntimeException {
    public DuplicatedAccountException(String message) {
        super(message);
    }

    public static DuplicatedAccountException of(long id) {
        return new DuplicatedAccountException(
                String.format("La cuenta de origen con Id %d es la misma que la cuenta de destino. Esta opreación no está permitida", id)
        );
    }
}
