package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.TypeOperationRequestDTO;
import com.siqueiros.bank.dto.TypeOperationResponseDTO;

import java.util.List;

public interface TypeOperationService {
    List<TypeOperationResponseDTO> getAllTypeOperations();
    TypeOperationResponseDTO createTypeOperation(TypeOperationRequestDTO request);
    TypeOperationResponseDTO updateTypeOperation(Long id, TypeOperationRequestDTO request);
    TypeOperationResponseDTO deleteTypeOperation(Long id);
    TypeOperationResponseDTO findTypeOperationById(Long id);
}
