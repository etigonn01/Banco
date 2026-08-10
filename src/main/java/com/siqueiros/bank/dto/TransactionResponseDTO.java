package com.siqueiros.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        String sourceAccountFullName,
        String destinationAccountFullName,
        BigDecimal amount,
        LocalDateTime transactionDateTime
)
{}
