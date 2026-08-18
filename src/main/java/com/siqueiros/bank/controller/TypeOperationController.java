package com.siqueiros.bank.controller;

import com.siqueiros.bank.dto.TypeOperationRequestDTO;
import com.siqueiros.bank.dto.TypeOperationResponseDTO;
import com.siqueiros.bank.service.TypeOperationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/type-operations")
public class TypeOperationController {

    private final TypeOperationService typeOperationService;

    public TypeOperationController(TypeOperationService typeOperationService) {
        this.typeOperationService = typeOperationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TypeOperationResponseDTO> getTypeOperations() {
        return typeOperationService.getAllTypeOperations();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeOperationResponseDTO createTypeOperation(@Valid @RequestBody TypeOperationRequestDTO request) {
        return typeOperationService.createTypeOperation(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TypeOperationResponseDTO updateTypeOperation(@PathVariable Long id, @Valid @RequestBody TypeOperationRequestDTO request) {
        return typeOperationService.updateTypeOperation(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TypeOperationResponseDTO deleteTypeOperation(@PathVariable Long id) {
        return typeOperationService.deleteTypeOperation(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TypeOperationResponseDTO getTypeOperation(@PathVariable Long id) {
        return typeOperationService.findTypeOperationById(id);
    }
}
