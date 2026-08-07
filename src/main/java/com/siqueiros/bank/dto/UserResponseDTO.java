package com.siqueiros.bank.dto;
import java.time.LocalDateTime;

public record UserResponseDTO(Long id, String fullname, String email, String passwordHash, String phoneNumber, LocalDateTime createdAt) {
}
