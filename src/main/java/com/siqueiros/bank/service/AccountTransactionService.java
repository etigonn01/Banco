package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.AccountTransactionRequestDTO;
import com.siqueiros.bank.dto.AccountTransactionResponseDTO;

import java.util.List;

public interface AccountTransactionService {
    AccountTransactionResponseDTO createTransaction(AccountTransactionRequestDTO dto);
    List<AccountTransactionResponseDTO> getTransactionsByAccountId(Long accountId);
}
