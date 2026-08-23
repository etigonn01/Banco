package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.AccountRequestDTO;
import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.exception.DuplicatedAccountException;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.mappers.AccountMapper;
import com.siqueiros.bank.model.Account;
import com.siqueiros.bank.repositories.AccountRepository;
import com.siqueiros.bank.repositories.TypeAccountRepository;
import com.siqueiros.bank.repositories.UserRepository;
import com.siqueiros.bank.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService
{
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TypeAccountRepository typeAccountRepository;
    private final AccountMapper accountMapper;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            UserRepository userRepository,
            TypeAccountRepository typeAccountRepository,
            AccountMapper accountMapper)
    {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.typeAccountRepository = typeAccountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public List<AccountResponseDTO> findAllAccounts() {
        return accountRepository.findAllWithRelations()
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    public AccountResponseDTO findByAccountId(Long accountId) {
        return accountRepository.findByIdWithRelations(accountId)
                .map(accountMapper::toResponse)
                .orElseThrow(() -> EntityNotFoundException.of("Cuenta",  accountId));
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        boolean isDuplicatedAccount = accountRepository
                .existsByUserIdAndTypeAccountId(accountRequestDTO.userId(), accountRequestDTO.typeAccountId());

        if (isDuplicatedAccount) {
            throw DuplicatedAccountException.of(accountRequestDTO.userId());
        }

        var user = userRepository.findById(accountRequestDTO.userId())
                .orElseThrow(() -> EntityNotFoundException.of("Usuario", accountRequestDTO.userId()));

        var typeAccount = typeAccountRepository.findById(accountRequestDTO.typeAccountId())
                .orElseThrow(() -> EntityNotFoundException.of("Tipo de cuenta", accountRequestDTO.typeAccountId()));

        Account newAccount = new Account(accountRequestDTO.balance(), typeAccount, user);
        var accountSaved = accountRepository.save(newAccount);
        return accountMapper.toResponse(accountSaved);
    }

    @Override
    public AccountResponseDTO updateAccount(Long accountId, Long typeAccountId) {
        var typeAccount = typeAccountRepository.findById(typeAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cuenta no  encontrada. Id: " + typeAccountId));

        var account  = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada. Id: " + accountId));

        account.setTypeAccount(typeAccount);
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
