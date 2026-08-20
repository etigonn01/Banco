package com.siqueiros.bank.controller;

import com.siqueiros.bank.dto.AccountRequestDTO;
import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponseDTO> findAll(){
        return accountService.findAllAccounts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDTO save(@Valid @RequestBody AccountRequestDTO accountRequestDTO){
        return accountService.createAccount(accountRequestDTO);
    }

    @PutMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDTO update(@PathVariable Long accountId, @RequestBody AccountRequestDTO request){
        return accountService.updateAccount(accountId, request.typeAccountId());
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDTO delete(@PathVariable Long accountId){
        return accountService.deleteAccount(accountId);
    }

    @GetMapping("/search/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDTO getById(@PathVariable Long accountId){
        return accountService.findByAccountId(accountId);
    }
}
