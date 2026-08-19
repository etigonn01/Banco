package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.TypeAccountRequestDTO;
import com.siqueiros.bank.dto.TypeAccountResponseDTO;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.model.TypeAccount;
import com.siqueiros.bank.model.TypeOperation;
import com.siqueiros.bank.repositories.TypeAccountRepository;
import com.siqueiros.bank.service.TypeAccountService;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class TypeAccountServiceImpl implements TypeAccountService
{
    private final TypeAccountRepository typeAccountRepository;

    public TypeAccountServiceImpl(TypeAccountRepository typeAccountRepository) {
        this.typeAccountRepository = typeAccountRepository;
    }

    @Override
    public List<TypeAccountResponseDTO> getAllTypeAccounts() {
        return typeAccountRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TypeAccountResponseDTO getTypeAccountById(Long id) {
        return typeAccountRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cuenta no encontrada. Id:" + id));
    }

    @Override
    public TypeAccountResponseDTO createTypeAccount(TypeAccountRequestDTO request) {
        if (typeAccountRepository.findByName(request.name().toUpperCase()).isPresent()) {
            throw new DataIntegrityViolationException("El tipo de cuenta proporcionada ya está registrado y en uso");
        }
        TypeAccount typeOperation = new TypeAccount(request.name().toLowerCase());
        return mapToResponse(this.typeAccountRepository.save(typeOperation));
    }

    @Override
    public TypeAccountResponseDTO updateTypeAccount(Long id, TypeAccountRequestDTO request) {
        TypeAccount typeAccount = typeAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cuenta no encontrada. Id:" + id));

        typeAccountRepository.findByName(request.name().toLowerCase())
                .ifPresent(existingTypeAccount -> {
                    if (!existingTypeAccount.getId().equals(id)) {
                        throw new DataIntegrityViolationException("El tipo de cuenta proporcionada ya está registrado y en uso");
                    }
                });
        typeAccount.setName(request.name());
        return mapToResponse(this.typeAccountRepository.save(typeAccount));
    }

    @Override
    public TypeAccountResponseDTO deleteTypeAccount(Long id) {
        TypeAccount typeAccount = typeAccountRepository.findById(id)
                .orElseThrow(() -> new  EntityNotFoundException("Tipo de cuenta no encontrada. Id:" + id));
        typeAccountRepository.delete(typeAccount);
        return mapToResponse(typeAccount);
    }

    private TypeAccountResponseDTO mapToResponse(TypeAccount typeAccount) {
        return new TypeAccountResponseDTO(
                typeAccount.getId(),
                typeAccount.getName()
        );
    }
}
