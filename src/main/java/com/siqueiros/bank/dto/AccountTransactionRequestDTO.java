package com.siqueiros.bank.dto;

import java.math.BigDecimal;

public record AccountTransactionRequestDTO(
        Long typeOperationId,
        Long accountId,
        BigDecimal amount
)
{}
