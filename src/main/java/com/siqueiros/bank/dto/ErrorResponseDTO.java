package com.siqueiros.bank.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String path,
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {
}
