package com.siqueiros.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResquestDTO(
        Long typeOperationId,
        Long accountId,
        BigDecimal amount,
        LocalDateTime trasactionDateTime
) {}
