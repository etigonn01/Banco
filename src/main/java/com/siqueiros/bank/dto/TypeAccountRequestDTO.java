package com.siqueiros.bank.dto;

import jakarta.validation.constraints.Size;

public record TypeAccountRequestDTO(
       @Size(min = 2, max = 20, message = "El tipo de cuenta debe tener entre 2 y 20 caracteres")
       String name
)
{}
