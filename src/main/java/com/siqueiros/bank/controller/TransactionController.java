package com.siqueiros.bank.controller;

import com.siqueiros.bank.dto.TransactionRequestDTO;
import com.siqueiros.bank.dto.TransactionResponseDTO;
import com.siqueiros.bank.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionService.getAllTransaction();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponseDTO createTransaction(@RequestBody TransactionRequestDTO request) {
        return transactionService.registerTransaction(request);
    }

    @GetMapping("/search/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponseDTO> getTransactionByAccountId(@PathVariable Long id) {
        return transactionService.getTransactionsByAccountId(id);
    }

}
