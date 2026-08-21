package com.siqueiros.bank.repositories;

import com.siqueiros.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findTransactionBySourceAccountId(long id);
}
