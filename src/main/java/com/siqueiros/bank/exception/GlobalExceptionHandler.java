package com.siqueiros.bank.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.siqueiros.bank.dto.ErrorResponseDTO;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                "Los datos enviados en la petición no cumplen con los requisitos",
                LocalDateTime.now()
        );
        return  new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");

        String parameterName = ex.getName();
        String sentValue = String.valueOf(ex.getValue());
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido";
        String errorMessage = String.format("El párametro '%s' debe ser un número entero válido de tipo '%s'. Se recibió el valor inválido: '%s'",
                parameterName, requiredType, sentValue);

        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage,
                LocalDateTime.now()
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
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientFunds(InsufficientFundsException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");
        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
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
                LocalDateTime.now()
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
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicatedAccount(AccountAlreadyExistsException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");

        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return  new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AmountIsLessThanOrEqualToZero.class)
    public ResponseEntity<ErrorResponseDTO> handleAmountIsLessThanOrEqualToZero(AmountIsLessThanOrEqualToZero ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");

        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NegativeInitialBalanceException.class)
    public ResponseEntity<ErrorResponseDTO> handleNegativeInitialBalance(NegativeInitialBalanceException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");

        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountWithBalanceException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccountWithBalance(AccountWithBalanceException ex, WebRequest request) {
        String cleanPath = request.getDescription(false).replace("uri=", "");

        ErrorResponseDTO errorBody = new ErrorResponseDTO(
                cleanPath,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }
}
