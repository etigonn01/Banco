package com.siqueiros.bank.repositories;

import com.siqueiros.bank.model.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOperationRepository extends JpaRepository<TypeOperation, Long>{
}
