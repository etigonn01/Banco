package com.siqueiros.bank.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException of(String entityName, long id) {
        return new EntityNotFoundException(
                String.format("No se encontró el recurso '%s' con ID: %d", entityName, id)
        );
    }
}
