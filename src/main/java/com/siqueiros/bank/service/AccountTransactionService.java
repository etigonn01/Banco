package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.AccountTransactionRequestDTO;
import com.siqueiros.bank.dto.AccountTransactionResponseDTO;

import java.util.List;

public interface AccountTransactionService {
    List<AccountTransactionResponseDTO> getAllAccountTransactions();
    AccountTransactionResponseDTO createTransaction(AccountTransactionRequestDTO dto);
    List<AccountTransactionResponseDTO> findByAccountId(Long accountId);
}
