package com.siqueiros.bank.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@EntityListeners(EntityListeners.class)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "src_account", referencedColumnName = "id",nullable = false)
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "dest_account", referencedColumnName = "id", nullable = false)
    private Account destinationAccount;

    @Column(name = "amount", nullable = false, columnDefinition = "NUMERIC(12,2)")
    private BigDecimal amount;

    @CreatedDate
    @Column(name = "transaction_date", nullable = false,  updatable = false)
    private LocalDateTime transactionDateTime;

    public Transaction() {}
    public Transaction(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public String getSourceAccountFullName() {
        return this.sourceAccount.getUser().getFullName();
    }

    public String getDestinationAccountFullName() {
        return this.destinationAccount.getUser().getFullName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDateTime;
    }

    public void setTransactionDate(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }
}
