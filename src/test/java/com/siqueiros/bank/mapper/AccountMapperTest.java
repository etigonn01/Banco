package com.siqueiros.bank.mapper;

import com.siqueiros.bank.dto.AccountResponseDTO;
import com.siqueiros.bank.mappers.AccountMapper;
import com.siqueiros.bank.model.Account;
import com.siqueiros.bank.model.TypeAccount;
import com.siqueiros.bank.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AccountMapperTest {
    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapper();
    }

    @Test
    @DisplayName("Should map Account to AccountResponseDTO with all relationships")
    void shouldMapAccountToAccountResponseDTOWithAllRelations() {
        // preparación
        User user = new User();
        user.setFullName("Manuel Molina");

        TypeAccount typeAccount = new TypeAccount();
        typeAccount.setName("nomina");

        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(1500));
        account.setUser(user);
        account.setTypeAccount(typeAccount);
        account.setCreatedAt(LocalDateTime.now());

        // acción
        AccountResponseDTO response = accountMapper.toResponse(account);

        // verificación
        assertNotNull(response);
        assertEquals(account.getId(), response.id());
        assertEquals(account.getBalance(), response.balance());
        assertEquals("Manuel Molina", response.accountHolder());
        assertEquals("nomina", response.typeAccountName());
        assertEquals(account.getCreatedAt(), response.createdAt());
    }

    @Test
    @DisplayName("Should handle null values in relationships without throwing a NullPointerException")
    void shouldHandleNullValuesInRelationshipsWithoutThrowingNullPointerException() {
        // preparación
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(1500));
        account.setUser(null);
        account.setTypeAccount(null);
        account.setCreatedAt(LocalDateTime.now());

        // acción
        AccountResponseDTO response = accountMapper.toResponse(account);

        // verificación
        assertNotNull(response);
        assertNull(response.accountHolder());
        assertNull(response.typeAccountName());
    }

    @Test
    @DisplayName("Should return null if the received Account entity is null.")
    void  shouldReturnNullIfTheReceivedAccountEntityIsNull() {
        assertNull(accountMapper.toResponse(null));
    }
}
