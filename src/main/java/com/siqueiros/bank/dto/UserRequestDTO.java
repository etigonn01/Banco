package com.siqueiros.bank.dto;

public record UserRequest(String fullname, String email, String passwordHash, String phoneNumber) {
}
