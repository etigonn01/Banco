package com.siqueiros.bank.exception;

public class PayrollAccountAlreadyExistsException extends RuntimeException {
    public PayrollAccountAlreadyExistsException(String message) {
        super(message);
    }

    public static PayrollAccountAlreadyExistsException of(long id) {
        return new PayrollAccountAlreadyExistsException(
                String.format("El usuario con Id: %d ya tiene registrada una cuenta de este tipo", id)
        );
    }
}
