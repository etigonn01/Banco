package com.siqueiros.bank.controller;

import com.siqueiros.bank.dto.TypeAccountRequestDTO;
import com.siqueiros.bank.dto.TypeAccountResponseDTO;
import com.siqueiros.bank.service.TypeAccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/types-accounts")
public class TypeAccountController {
    private final TypeAccountService typeAccountService;

    public TypeAccountController(TypeAccountService typeAccountService) {
        this.typeAccountService = typeAccountService;
    }

    @GetMapping
    public List<TypeAccountResponseDTO> getAllTypeAccounts(){
        return typeAccountService.getAllTypeAccounts();
    }

    @PostMapping
    public TypeAccountResponseDTO createTypeAccount(@Valid @RequestBody TypeAccountRequestDTO typeAccountRequestDTO){
        return typeAccountService.createTypeAccount(typeAccountRequestDTO);
    }

    @PutMapping("/{id}")
    public TypeAccountResponseDTO updateTypeAccount(@PathVariable Long id, @Valid @RequestBody TypeAccountRequestDTO typeAccountRequestDTO){
        return typeAccountService.updateTypeAccount(id, typeAccountRequestDTO);
    }

    @DeleteMapping("/{id}")
    public TypeAccountResponseDTO deleteTypeAccount(@PathVariable Long id){
        return typeAccountService.deleteTypeAccount(id);
    }

    @GetMapping("/search/{id}")
    public TypeAccountResponseDTO searchTypeAccount(@PathVariable Long id){
        return typeAccountService.getTypeAccountById(id);
    }
}
