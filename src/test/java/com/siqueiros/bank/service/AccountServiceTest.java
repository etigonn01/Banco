package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.AccountRequestDTO;
import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.exception.DuplicatedAccountException;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.mappers.AccountMapper;
import com.siqueiros.bank.model.Account;
import com.siqueiros.bank.model.TypeAccount;
import com.siqueiros.bank.model.User;
import com.siqueiros.bank.repositories.AccountRepository;
import com.siqueiros.bank.repositories.TypeAccountRepository;
import com.siqueiros.bank.repositories.UserRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TypeAccountRepository typeAccountRepository;

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

    @Test
    @DisplayName("Should return AccountResponseDTO when the account exists by Id")
    void findAccountById_ShouldReturnAccountResponseDTOWhenTheAccountExists() {
        // preparación
        long accountId = 1L;
        Account mockAccount = new Account();
        mockAccount.setId(accountId);

        AccountResponseDTO expectedDTO = new AccountResponseDTO(
                accountId, new BigDecimal("500"), "nomina", "Manuel Molina", LocalDateTime.now()
        );

        // configuración de la respuesta
        when(accountRepository.findByIdWithRelations(accountId)).thenReturn(Optional.of(mockAccount));
        when(accountMapper.toResponse(mockAccount)).thenReturn(expectedDTO);

        // acción
        AccountResponseDTO result = accountService.findByAccountId(accountId);

        // verificación
        assertNotNull(result);
        assertEquals(accountId, result.id());
        assertEquals("Manuel Molina", result.accountHolder());

        verify(accountRepository, times(1)).findByIdWithRelations(accountId);
        verify(accountMapper, times(1)).toResponse(mockAccount);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when the account does not exists")
    void findAccountById_ShouldThrowEntityNotFoundExceptionWhenTheAccountDoesNotExists() {
        long accountId = 99L;

        when(accountRepository.findByIdWithRelations(accountId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> accountService.findByAccountId(accountId));

        assertEquals("No se encontró el recurso 'Cuenta' con ID: 99", ex.getMessage());

        verify(accountRepository, times(1)).findByIdWithRelations(accountId);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    @DisplayName("Should create an account successfully when all validations are correct")
    void createAccount_ShouldCreateAccountSuccessfullyWhenAllValidationsAreCorrect() {
        // preparación
        AccountRequestDTO request = new AccountRequestDTO(new BigDecimal(500), 1L, 2L);
        User mockUser = new  User();
        TypeAccount mockTypeAccount = new  TypeAccount();

        Account mockSavecAccount = new  Account(request.balance(), mockTypeAccount, mockUser);
        AccountResponseDTO expectedDTO = new AccountResponseDTO(
                5L, request.balance(), "nomina", "Manuel Molina", LocalDateTime.now()
        );

        // configuración de comportamiento
        when(accountRepository.existsByUserIdAndTypeAccountId(2L, 1L)).thenReturn(false);
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser));
        when(typeAccountRepository.findById(1L)).thenReturn(Optional.of(mockTypeAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(mockSavecAccount);
        when(accountMapper.toResponse(mockSavecAccount)).thenReturn(expectedDTO);

        // acción
        AccountResponseDTO result = accountService.createAccount(request);

        // verificaciones
        assertNotNull(result);
        assertEquals(5L, result.id());
        assertEquals(request.balance(), result.balance());

        verify(accountRepository, times(1)).save(any(Account.class));
        verify(accountMapper, times(1)).toResponse(mockSavecAccount);
    }

    @Test
    @DisplayName("Should throw DuplicatedAccountException if the user already has a account of this type")
    void createAccount_ShouldThrowDuplicatedAccountExceptionIfTheUserAlreadyHasAnAccountOfTheType() {
        // preparación
        AccountRequestDTO request = new AccountRequestDTO(new BigDecimal(500), 1L, 2L);

        // configuración de comportamiento
        when(accountRepository.existsByUserIdAndTypeAccountId(2L, 1L)).thenReturn(true);

        // acción
        assertThrows(DuplicatedAccountException.class, () -> accountService.createAccount(request));

        // verificación
        verify(accountRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository, typeAccountRepository, accountRepository);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if the user does not exists")
    void  createAccount_ShouldThrowEntityNotFoundExceptionIfTheUserDoesNotExists() {
        AccountRequestDTO request = new AccountRequestDTO(new BigDecimal(500), 2L, 1L);

        when(accountRepository.existsByUserIdAndTypeAccountId(1L, 2L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.createAccount(request));

        verify(typeAccountRepository, never()).findById(anyLong());
        verify(accountRepository, never()).save(any());
        verifyNoMoreInteractions(accountMapper);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException if the type account does not exists")
    void createAccount_ShouldThrowEntityNotFoundExceptionIfTheTypeAccountDoesNotExists() {
        AccountRequestDTO request = new AccountRequestDTO(new BigDecimal(500), 2L, 1L);
        User mockUser = new  User();

        when(accountRepository.existsByUserIdAndTypeAccountId(1L, 2L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(typeAccountRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.createAccount(request));

        verify(accountRepository, never()).save(any());
    }
}
