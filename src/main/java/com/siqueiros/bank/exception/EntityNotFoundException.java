package com.siqueiros.bank.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException raise(String entityName, long id) {
        return new EntityNotFoundException(
                String.format("%s no encontrada con Id: %d", entityName, id)
        );
    }
}
