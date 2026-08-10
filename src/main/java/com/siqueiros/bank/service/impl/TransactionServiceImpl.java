package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.TransactionRequestDTO;
import com.siqueiros.bank.dto.TransactionResponseDTO;
import com.siqueiros.bank.model.Transaction;
import com.siqueiros.bank.repositories.TransactionRepository;
import com.siqueiros.bank.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getAllTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getSourceAccount().getUser().getFullName(),
                        t.getDestinationAccount().getUser().getFullName(),
                        t.getAmount(),
                        t.getTransactionDate()
                ))
                .toList();
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactionByAccountId(Long accountId) {
        return List.of();
    }

    @Override
    public TransactionResponseDTO registerTransaction(TransactionRequestDTO request) {
        return null;
    }

    @Override
    public TransactionResponseDTO updateTransaction(Long transactionId, TransactionRequestDTO request) {
        return null;
    }

    @Override
    public TransactionResponseDTO deleteTransaction(Long transactionId) {
        return null;
    }
}
