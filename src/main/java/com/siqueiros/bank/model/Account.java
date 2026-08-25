package com.siqueiros.bank.model;

import com.siqueiros.bank.exception.AccountAlreadyDeletedException;
import com.siqueiros.bank.exception.AccountWithBalanceException;
import com.siqueiros.bank.exception.NegativeInitialBalanceException;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance", columnDefinition = "NUMERIC(12,2)")
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_account_id", referencedColumnName = "id",nullable = false)
    private TypeAccount typeAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime deletedAt;

    public Account() {}
    public Account(BigDecimal balance, TypeAccount typeAccount, User user) {
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw NegativeInitialBalanceException.of(String.format(String.valueOf(balance)));
        }
        this.balance = balance;
        this.typeAccount = typeAccount;
        this.user = user;
    }

    public void close() {
        if( this.deletedAt != null ) {
            throw AccountAlreadyDeletedException.of(this.id);
        }

        if (this.balance.compareTo(BigDecimal.ZERO) > 0) {
            throw AccountWithBalanceException.of(this.id);
        }

        this.deletedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public TypeAccount getTypeAccount() {
        return typeAccount;
    }
    public void setTypeAccount(TypeAccount typeAccount) {
        this.typeAccount = typeAccount;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDeletedAt() { return this.deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

}
