package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.TypeOperationRequestDTO;
import com.siqueiros.bank.dto.TypeOperationResponseDTO;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.model.TypeOperation;
import com.siqueiros.bank.repositories.TypeOperationRepository;
import com.siqueiros.bank.service.TypeOperationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class TypeOperationServiceImpl implements TypeOperationService {
    private final TypeOperationRepository typeOperationRepository;

    public  TypeOperationServiceImpl(TypeOperationRepository typeOperationRepository) {
        this.typeOperationRepository = typeOperationRepository;
    }

    @Override
    public List<TypeOperationResponseDTO> getAllTypeOperations() {
        return typeOperationRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TypeOperationResponseDTO createTypeOperation(TypeOperationRequestDTO request) {
        if (typeOperationRepository.findByName(request.name().toLowerCase()).isPresent()) {
            throw new DataIntegrityViolationException("El tipo de operación ya está registrada y en uso");
        }
        TypeOperation typeOperation = new TypeOperation(request.name().toLowerCase());
        return mapToResponse(this.typeOperationRepository.save(typeOperation));
    }

    @Override
    public TypeOperationResponseDTO updateTypeOperation(Long id, TypeOperationRequestDTO request) {
        TypeOperation typeOperation = typeOperationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de operación no encontrada. Id:" + id));

        typeOperationRepository.findByName(request.name())
                .ifPresent(existingType -> {
                    if(!existingType.getId().equals(id)) {
                        throw new DataIntegrityViolationException("El tipo de opreación proporcionada ya está registrada y en uso");
                    }
                });
        typeOperation.setName(request.name().toLowerCase());

        return mapToResponse(typeOperationRepository.save(typeOperation));
    }

    @Override
    public TypeOperationResponseDTO deleteTypeOperation(Long id) {
        TypeOperation typeOperation = typeOperationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de operación no encontrada. Id:" + id));
        typeOperationRepository.delete(typeOperation);
        return  mapToResponse(typeOperation);
    }

    @Override
    public TypeOperationResponseDTO findTypeOperationById(Long id) {
        return typeOperationRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de operación no encontrada. Id:" + id));
    }

    private TypeOperationResponseDTO mapToResponse(TypeOperation typeOperation) {
        return  new TypeOperationResponseDTO(
                typeOperation.getId(),
                typeOperation.getName()
        );
    }
}
