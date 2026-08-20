package com.siqueiros.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponseDTO(
        Long id,
        BigDecimal balance,
        String typeAccountName,
        String accountHolder,
        LocalDateTime createdAt
)
{}
