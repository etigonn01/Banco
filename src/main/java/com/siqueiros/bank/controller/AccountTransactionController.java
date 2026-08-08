package com.siqueiros.bank.controller;

import com.siqueiros.bank.dto.TransactionRequestDTO;
import com.siqueiros.bank.dto.TransactionResponseDTO;
import com.siqueiros.bank.service.AccountTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class AccountTransactionController {
    private final AccountTransactionService transactionService;

    public AccountTransactionController(AccountTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO create(@RequestBody TransactionRequestDTO request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/{accountId}")
    public List<TransactionResponseDTO> getAllTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }
}
