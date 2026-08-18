package com.siqueiros.bank.dto;

import jakarta.validation.constraints.Size;

public record TypeOperationRequestDTO(
        @Size(min = 2, max= 20, message = "El nombre del tipo de operación debe estar entre 2 y 20 caracteres")
        String name
) {
}
