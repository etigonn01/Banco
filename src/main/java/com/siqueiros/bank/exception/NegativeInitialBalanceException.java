package com.siqueiros.bank.exception;

public class NegativeInitialBalanceException extends RuntimeException {
    public NegativeInitialBalanceException(String message) {
        super(message);
    }

    public static NegativeInitialBalanceException of(String balance){
        return new NegativeInitialBalanceException(
                String.format("El saldo inicial proporcionado no es válido. Saldo proporcionado: %s", balance)
        );
    }
}
