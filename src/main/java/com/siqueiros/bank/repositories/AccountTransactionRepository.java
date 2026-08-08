package com.siqueiros.bank.repositories;

import com.siqueiros.bank.model.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountTransaction, Long>{
}
