package com.siqueiros.bank.repositories;

import com.siqueiros.bank.model.TypeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeAccountRepository extends JpaRepository<TypeAccount, Long>{
    Optional<TypeAccount> findByName(String name);
}
