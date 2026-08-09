package com.siqueiros.bank.repositories;

import com.siqueiros.bank.model.TypeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<TypeAccount, Long>{
}
