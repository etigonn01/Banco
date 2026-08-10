package com.siqueiros.bank.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_transactions")
public class AccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_operation_id", referencedColumnName = "id", nullable = false)
    private TypeOperation typeOperation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id",nullable = false)
    private Account account;

    @Column(name = "amount", nullable = false, columnDefinition = "NUMERIC(12,2)")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "transaction_datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime transactionDateTime;

    public AccountTransaction(){}
    public AccountTransaction(TypeOperation typeOperation, Account account, BigDecimal amount) {
        this.typeOperation = typeOperation;
        this.account = account;
        this.amount = amount;
        this.transactionDateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TypeOperation getTypeOperation() {
        return typeOperation;
    }
    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) { this.account = account; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public LocalDateTime getTransactionDateTime() { return transactionDateTime; }
    public void setTransactionDateTime(LocalDateTime transactionDateTime) { this.transactionDateTime = transactionDateTime; }
}
