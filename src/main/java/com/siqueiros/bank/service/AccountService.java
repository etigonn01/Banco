package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.AccountRequestDTO;
import com.siqueiros.bank.dto.AccountResponseDTO;

import java.util.List;

public interface AccountService {
    List<AccountResponseDTO> findAllAccounts();
    AccountResponseDTO findByAccountId(Long accountId);
    AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO);
    AccountResponseDTO updateAccount(Long accountId, Long typeAccountId);
    AccountResponseDTO deleteAccount(Long accountId);
}
