package com.siqueiros.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequestDTO(
        Long typeOperationId,
        Long accountId,
        BigDecimal amount
) {}
