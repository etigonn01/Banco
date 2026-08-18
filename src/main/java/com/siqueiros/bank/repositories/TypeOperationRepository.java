package com.siqueiros.bank.repositories;

import com.siqueiros.bank.dto.TypeOperationRequestDTO;
import com.siqueiros.bank.model.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeOperationRepository extends JpaRepository<TypeOperation, Long>{
    Optional<TypeOperation> findByName(String name);
}
