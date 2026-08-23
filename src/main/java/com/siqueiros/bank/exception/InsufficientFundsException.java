package com.siqueiros.bank.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }

    public static InsufficientFundsException of(String balance) {
        return new InsufficientFundsException(
                String.format("El saldo es invalido. Saldo actual: $ %s", balance)
        );
    }
}
