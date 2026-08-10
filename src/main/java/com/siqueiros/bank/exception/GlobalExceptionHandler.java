package com.siqueiros.bank.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.siqueiros.bank.dto.ErrorResponseDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");
        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientFunds(InsufficientFundsException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");
        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request
    ) {
        String cleanPath = request.getDescription(false).replace("uri=", "");

        String message = "El correo electrónico proporcionado ya está registrado en el sistema.";

        if(ex.getMostSpecificCause().getMessage().contains("phone")) {
            message = "El número de teléfono proporcionado ya está registrado en el sistema.";
        }

        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
    }
}
