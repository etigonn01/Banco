package com.siqueiros.bank.exception;

public class AmountIsLessThanOrEqualToZero extends RuntimeException {
    public AmountIsLessThanOrEqualToZero(String message) {
        super(message);
    }
}
