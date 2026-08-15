package com.siqueiros.bank.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.siqueiros.bank.dto.ErrorResponseDTO;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> validations = new HashMap<>();
        String cleanPath = request.getDescription(false).replace("uri=", "");

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validations.put(fieldName, errorMessage);
        });

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Los datos enviados en la petición no cumplen con los requisitos",
                LocalDateTime.now(),
                validations
        );
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");
        String parameterName = ex.getName();
        String sendedValue = String.valueOf(ex.getValue());
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido";
        Map<String, String> validations = Map.of(
                parameterName, String.format("El párametro '%s' debe ser un número entero válido de tipo '%s'. Se recibió el valor inválido: '%s'",
                parameterName, requiredType, sendedValue)
            );
        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Error de validación",
                LocalDateTime.now(),
                validations
        );
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");
        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
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
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request
    ) {
        String cleanPath = request.getDescription(false).replace("uri=", "");

        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");
        String parameterName = ex.getParameterName();
        String parameterType = ex.getParameterType();
        String errorMessage = String.format("La petición no recibió el párametro requerido '%s' de tipo '%s'", parameterName, parameterType);
        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage,
                LocalDateTime.now(),
                null
        );
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }
}
