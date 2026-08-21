package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.AccountTransactionRequestDTO;
import com.siqueiros.bank.dto.AccountTransactionResponseDTO;
import com.siqueiros.bank.enums.OperationStrategy;
import com.siqueiros.bank.enums.OperationStrategyFactory;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.mappers.AccountTransactionMapper;
import com.siqueiros.bank.model.Account;
import com.siqueiros.bank.model.AccountTransaction;
import com.siqueiros.bank.model.TypeOperation;
import com.siqueiros.bank.repositories.AccountRepository;
import com.siqueiros.bank.repositories.AccountTransactionRepository;
import com.siqueiros.bank.repositories.TypeOperationRepository;
import com.siqueiros.bank.service.AccountTransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {
    private final AccountTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TypeOperationRepository typeOperationRepository;

    private final OperationStrategyFactory strategyFactory;
    private final AccountTransactionMapper transactionMapper;

    public AccountTransactionServiceImpl(
            AccountTransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TypeOperationRepository typeOperationRepository,
            OperationStrategyFactory strategyFactory,
            AccountTransactionMapper transactionMapper
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.typeOperationRepository = typeOperationRepository;
        this.strategyFactory = strategyFactory;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public AccountTransactionResponseDTO createTransaction(AccountTransactionRequestDTO request) {
        Account account = accountRepository.findByIdWithLock(request.accountId())
                .orElseThrow(() ->  EntityNotFoundException.of("Cuenta", request.accountId()));

        TypeOperation typeOperation = typeOperationRepository.findById(request.typeOperationId())
                .orElseThrow(() -> EntityNotFoundException.of("Tipo de cuenta", request.typeOperationId()));

        OperationStrategy strategy = strategyFactory.getStrategy(typeOperation.getName());
        strategy.execute(account, request.amount());

        AccountTransaction savedTransaction = transactionRepository.save(new AccountTransaction(typeOperation, account, request.amount()));
        return transactionMapper.toResponseDTO(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountTransactionResponseDTO> findByAccountId(Long accountId) {
        List<AccountTransaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.stream()
                .map(t -> new AccountTransactionResponseDTO(
                        t.getId(),
                        t.getTypeOperation().getName(),
                        t.getAccount().getUser().getFullName(),
                        t.getAmount(),
                        t.getTransactionDateTime()
                ))
                .toList();
    }
}
