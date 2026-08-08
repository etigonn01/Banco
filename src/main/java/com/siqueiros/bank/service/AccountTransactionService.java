package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.TransactionRequestDTO;
import com.siqueiros.bank.dto.TransactionResponseDTO;

import java.util.List;

public interface AccountTransactionService {
    TransactionResponseDTO createTransaction(TransactionRequestDTO dto);
    List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId);
}
