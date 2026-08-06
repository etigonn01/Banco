package com.siqueiros.bank.repositories;

import com.siqueiros.bank.model.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long>{
}
