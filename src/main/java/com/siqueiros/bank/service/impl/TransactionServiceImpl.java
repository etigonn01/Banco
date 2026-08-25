package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.TransactionRequestDTO;
import com.siqueiros.bank.dto.TransactionResponseDTO;
import com.siqueiros.bank.enums.TransferenciaTransactionStrategy;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.mappers.TransactionMapper;
import com.siqueiros.bank.model.Transaction;
import com.siqueiros.bank.repositories.AccountRepository;
import com.siqueiros.bank.repositories.TransactionRepository;
import com.siqueiros.bank.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final TransferenciaTransactionStrategy strategy;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TransactionMapper transactionMapper,
            TransferenciaTransactionStrategy strategy) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
        this.strategy = strategy;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getAllTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(this.transactionMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
        var transactionById = transactionRepository.findTransactionBySourceAccountId(accountId);
        return transactionById.stream()
                .map(this.transactionMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public TransactionResponseDTO registerTransaction(TransactionRequestDTO request) {
        var sourceAccount = accountRepository.findActiveByIdWithRowLevelLocking(request.sourceAccountId())
                .orElseThrow(() -> EntityNotFoundException.of("Cuenta de origen",  request.sourceAccountId()));

        var destinationAccount = accountRepository.findActiveByIdWithRowLevelLocking(request.destinationAccountId())
                .orElseThrow(() -> EntityNotFoundException.of("Cuenta de destino",  request.destinationAccountId()));

        strategy.execute(sourceAccount, destinationAccount, request.amount());
        var transaction = new Transaction(sourceAccount, destinationAccount, request.amount());

        return transactionMapper.toResponseDTO(transactionRepository.save(transaction));
    }
}
