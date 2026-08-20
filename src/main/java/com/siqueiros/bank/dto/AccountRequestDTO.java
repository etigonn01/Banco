package com.siqueiros.bank.dto;

import java.math.BigDecimal;

public record AccountRequestDTO(
        BigDecimal balance,
        Long typeAccountId,
        Long userId
)
{
    public AccountRequestDTO(BigDecimal balance, Long typeAccountId, Long userId){
        this.balance = balance;
        this.typeAccountId = typeAccountId;
        this.userId = userId;
    }
    public AccountRequestDTO(Long typeAccountId){
        this(null, typeAccountId, null);
    }
}
