package com.siqueiros.bank.controller;

import com.siqueiros.bank.dto.AccountTransactionRequestDTO;
import com.siqueiros.bank.dto.AccountTransactionResponseDTO;
import com.siqueiros.bank.service.AccountTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/account-trasactions")
public class AccountTransactionController {
    private final AccountTransactionService accountTransactionService;

    public AccountTransactionController(AccountTransactionService accountTransactionService) {
        this.accountTransactionService = accountTransactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountTransactionResponseDTO create(@RequestBody AccountTransactionRequestDTO request) {
        return accountTransactionService.createTransaction(request);
    }

    @GetMapping("/{accountId}")
    public List<AccountTransactionResponseDTO> getAllTransactionsByAccountId(@PathVariable Long accountId) {
        return accountTransactionService.getTransactionsByAccountId(accountId);
    }
}
