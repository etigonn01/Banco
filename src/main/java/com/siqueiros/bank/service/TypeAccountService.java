package com.siqueiros.bank.service;


import com.siqueiros.bank.dto.TypeAccountRequestDTO;
import com.siqueiros.bank.dto.TypeAccountResponseDTO;

import java.util.List;

public interface TypeAccountService {
    List<TypeAccountResponseDTO> getAllTypeAccounts();
    TypeAccountResponseDTO getTypeAccountById(Long id);
    TypeAccountResponseDTO createTypeAccount(TypeAccountRequestDTO request);
    TypeAccountResponseDTO updateTypeAccount(Long id, TypeAccountRequestDTO request);
    TypeAccountResponseDTO deleteTypeAccount(Long id);
}
