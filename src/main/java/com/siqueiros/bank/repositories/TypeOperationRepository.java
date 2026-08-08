package com.siqueiros.bank.repositories;

import com.siqueiros.bank.model.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationTypeRepository extends JpaRepository<TypeOperation, Long>{
}
