package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.TransactionRequestDTO;
import com.siqueiros.bank.dto.TransactionResponseDTO;
import java.util.List;

public interface TransactionService {
    List<TransactionResponseDTO> getAllTransaction();
    List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId);
    TransactionResponseDTO registerTransaction(TransactionRequestDTO request);
}
