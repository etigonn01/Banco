package com.siqueiros.bank.dto;

public record UserRequestDTO(
        String fullName,
        String email,
        String passwordHash,
        String phoneNumber
) {}
