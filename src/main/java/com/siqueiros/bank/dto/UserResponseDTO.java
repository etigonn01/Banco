package com.siqueiros.bank.dto;
import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String fullName,
        String email,
        String passwordHash,
        String phoneNumber,
        LocalDateTime createdAt
)
{}
