package com.siqueiros.bank.dto;

import java.util.List;

public record ApiInfoResponseDTO(String greeting, String version, String author, List<String> technologies) {
}
