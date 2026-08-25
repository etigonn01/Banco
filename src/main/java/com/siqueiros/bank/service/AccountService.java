package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.AccountRequestDTO;
import com.siqueiros.bank.dto.AccountResponseDTO;

import java.util.List;

public interface AccountService {
    List<AccountResponseDTO> findAllActiveAccounts();
    AccountResponseDTO findActiveAccountById(Long accountId);
    AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO);
    void logicalDelete(Long accountId);
}
