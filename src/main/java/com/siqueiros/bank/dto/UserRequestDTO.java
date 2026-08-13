package com.siqueiros.bank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @Size(min = 8, max = 120, message = "El nombre debe tener entre 8 y 120 caracteres")
        String fullName,

        @Size(min = 8, max = 50, message = "El correo electrónico debe tener entre 8 y 50 caracteres")
        @Email(message = "El formato del correo electrónico no es valido")
        String email,

        // TODO: implentar una función de encriptado de contraseña y validar la prueba unitaria
        @NotBlank(message = "El hash de la contraseña no puede estar vació")
        String passwordHash,

        @Size(min = 10, max = 10, message = "El número de teléfono debe ser un formato de 10 dígitos")
        String phoneNumber
) {}
