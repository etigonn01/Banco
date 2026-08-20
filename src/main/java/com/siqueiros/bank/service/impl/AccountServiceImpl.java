package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.AccountRequestDTO;
import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.exception.DuplicatedAccountException;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.exception.InsufficientFundsException;
import com.siqueiros.bank.model.Account;
import com.siqueiros.bank.repositories.AccountRepository;
import com.siqueiros.bank.repositories.TypeAccountRepository;
import com.siqueiros.bank.repositories.UserRepository;
import com.siqueiros.bank.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService
{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TypeAccountRepository typeAccountRepository;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            UserRepository userRepository,
            TypeAccountRepository typeAccountRepository)
    {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.typeAccountRepository = typeAccountRepository;
    }

    @Override
    public List<AccountResponseDTO> findAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AccountResponseDTO findByAccountId(Long accountId) {
        return accountRepository.findById(accountId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada. Id: " + accountId));
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        var user = userRepository.findById(accountRequestDTO.userId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado. Id: " +  accountRequestDTO.userId()));

        var typeAccount = typeAccountRepository.findById(accountRequestDTO.typeAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cuenta no encontrada. Id: " + accountRequestDTO.typeAccountId()));

        boolean isDuplicatedAccount = accountRepository.existsByUserIdAndTypeAccountID(accountRequestDTO.userId(), accountRequestDTO.typeAccountId());
        if (isDuplicatedAccount) {
            throw new DuplicatedAccountException("El usuario ya tiene registrada una cuenta de este tipo. Solo se permite un tipo de cuenta por usuario");
        }

        if (accountRequestDTO.balance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("La cuenta no puede crearse con saldo negativo. Saldo actual: " +  accountRequestDTO.balance());
        }

        Account newAccount = new Account(accountRequestDTO.balance(), typeAccount, user, LocalDateTime.now());
        var accountSaved = accountRepository.save(newAccount);
        return mapToResponse(accountSaved);
    }

    @Override
    public AccountResponseDTO updateAccount(Long accountId, AccountRequestDTO accountRequestDTO) {
        var user  = userRepository.findById(accountRequestDTO.userId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado. Id: " + accountRequestDTO.userId()));
        var typeAccount = typeAccountRepository.findById(accountRequestDTO.typeAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cuenta no  encontrada. Id: " + accountRequestDTO.typeAccountId()));
        if (accountRequestDTO.balance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("La cuenta no puede actualizarse con saldo negativo. Saldo actual: " +  accountRequestDTO.balance());
        }
        var account  = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada. Id: " + accountId));
        account.setBalance(accountRequestDTO.balance());
        account.setTypeAccount(typeAccount);
        account.setUser(user);
        return mapToResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponseDTO deleteAccount(Long accountId) {
        var account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada. Id: " + accountId));
        accountRepository.delete(account);
        return mapToResponse(account);
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
