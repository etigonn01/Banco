package com.siqueiros.bank.dto;

public record UserRequestDTO(String fullname, String email, String passwordHash, String phoneNumber) {
}
