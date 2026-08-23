package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.mappers.AccountMapper;
import com.siqueiros.bank.model.Account;
import com.siqueiros.bank.repositories.AccountRepository;
import com.siqueiros.bank.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Should return a list of DTOs when there are accounts in the database")
    void findAllAccounts_ShouldReturnAListOfDTOsWhenThereAreAccountsInTheDatabase() {
        // preparación
        Account account1 =  new Account();
        Account account2 =  new Account();
        List<Account> mockAccounts = List.of(account1, account2);

        AccountResponseDTO dto1 = new AccountResponseDTO(
                1L,
                new BigDecimal("500.0"),
                "nomina",
                "Manuel Molina",
                LocalDateTime.now()
        );

        AccountResponseDTO dto2 = new AccountResponseDTO(
                2L,
                new BigDecimal("1500.0"),
                "ahorro",
                "Sofia Contreras",
                LocalDateTime.now()
        );

        // configuración de Mocks (Comportamiento esperado)
        when(accountRepository.findAllWithRelations()).thenReturn(mockAccounts);
        when(accountMapper.toResponse(account1)).thenReturn(dto1);
        when(accountMapper.toResponse(account2)).thenReturn(dto2);

        // acción
        List<AccountResponseDTO> result = accountService.findAllAccounts();

        // verificación
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ahorro", dto2.typeAccountName());

        verify(accountRepository, times(1)).findAllWithRelations();
        verify(accountMapper, times(2)).toResponse(any(Account.class));
    }

    @Test
    @DisplayName("Should return an empty list when there are no accounts")
    void findAllAccounts_ShouldReturnAnEmptyListWhenThereAreNoAccounts() {
        // preparación
        when(accountRepository.findAllWithRelations()).thenReturn(List.of());

        // acción
        List<AccountResponseDTO> result = accountService.findAllAccounts();

        // verificación
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(accountRepository, times(1)).findAllWithRelations();
        verify(accountMapper, never()).toResponse(any(Account.class));
    }
}
