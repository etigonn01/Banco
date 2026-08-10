package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.TransactionRequestDTO;
import com.siqueiros.bank.dto.TransactionResponseDTO;
import java.util.List;

public interface TransactionService {
    List<TransactionResponseDTO> getAllTransaction();
    List<TransactionResponseDTO> getAllTransactionByAccountId(Long accountId);
    TransactionResponseDTO registerTransaction(TransactionRequestDTO request);
    TransactionResponseDTO updateTransaction(Long transactionId, TransactionRequestDTO request);
    TransactionResponseDTO deleteTransaction(Long transactionId);
}
