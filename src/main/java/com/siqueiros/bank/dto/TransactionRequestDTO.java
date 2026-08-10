package com.siqueiros.bank.dto;

import com.siqueiros.bank.model.Account;
import java.math.BigDecimal;

public record TransactionRequestDTO(
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal amount
) {}
