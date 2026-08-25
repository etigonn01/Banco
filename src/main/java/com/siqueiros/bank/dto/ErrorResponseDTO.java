package com.siqueiros.bank.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        String path,
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {}
