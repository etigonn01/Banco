package com.siqueiros.bank.dto;

import java.math.BigDecimal;

public record AccountRequestDTO(
        BigDecimal balance,
        Long typeAccountId,
        Long userId
)
{}
