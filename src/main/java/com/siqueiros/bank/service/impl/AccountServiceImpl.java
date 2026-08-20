package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.AccountRequestDTO;
import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.model.Account;
import com.siqueiros.bank.repositories.AccountRepository;
import com.siqueiros.bank.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService
{
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<AccountResponseDTO> findAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AccountResponseDTO findByAccountId(Long id) {
        return accountRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada. Id: " + id));
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        
        return null;
    }

    @Override
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO accountRequestDTO) {
        return null;
    }

    @Override
    public AccountResponseDTO deleteAccount(Long id) {
        return null;
    }

    private AccountResponseDTO mapToResponse(Account account) {
        return new AccountResponseDTO(
                account.getId(),
                account.getBalance(),
                account.getTypeAccount().getName(),
                account.getUser().getFullName(),
                account.getCreatedAt()
        );
    }
}
