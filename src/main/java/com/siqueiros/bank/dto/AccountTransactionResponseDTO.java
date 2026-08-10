package com.siqueiros.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountTransactionResponseDTO(
        Long id,
        String operationName,
        String accountHolderName,
        BigDecimal amount,
        LocalDateTime trasactionDateTime
)
{}
