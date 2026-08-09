package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.TransactionRequestDTO;
import com.siqueiros.bank.dto.TransactionResponseDTO;
import com.siqueiros.bank.exception.AccountNotFoundException;
import com.siqueiros.bank.exception.InsufficientFundsException;
import com.siqueiros.bank.exception.TypeOperationNotFoundException;
import com.siqueiros.bank.exception.UserNotFoundException;
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

    public AccountTransactionServiceImpl(
            AccountTransactionRepository transactionRepository,
            AccountRepository accountRepository,
            TypeOperationRepository typeOperationRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.typeOperationRepository = typeOperationRepository;
    }

    @Override
    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
        Account account = accountRepository.findById(dto.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada con Id: " + dto.accountId()));

        TypeOperation typeOperation = typeOperationRepository.findById(dto.typeOperationId())
                .orElseThrow(() -> new TypeOperationNotFoundException("Tipo de operación no encontrada con Id: " + dto.typeOperationId()));


        if("Retiro".equalsIgnoreCase(typeOperation.getName())) {
            if(account.getBalance().compareTo(dto.amount()) < 0) {
                throw new InsufficientFundsException("Fondos insuficientes para realizar el retiro.");
            }
            account.setBalance(account.getBalance().subtract(dto.amount()));
        } else if ("Deposito".equalsIgnoreCase(typeOperation.getName())) {
            account.setBalance(account.getBalance().add(dto.amount()));
        } else {
            throw new RuntimeException("Operación no disponible.");
        }

        AccountTransaction transaction = new AccountTransaction(
                typeOperation,
                account,
                dto.amount()
        );

        accountRepository.save(account);
        AccountTransaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponseDTO(
                savedTransaction.getId(),
                savedTransaction.getTypeOperation().getName(),
                savedTransaction.getAccount().getUser().getFullName(),
                savedTransaction.getAmount(),
                savedTransaction.getTransactionDateTime()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getTransactionsByAccountId(Long accountId) {
        List<AccountTransaction> transactions = transactionRepository.findTransactionsByAccountId(accountId);
        return transactions.stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getTypeOperation().getName(),
                        t.getAccount().getUser().getFullName(),
                        t.getAmount(),
                        t.getTransactionDateTime()
                ))
                .toList();
    }
}
