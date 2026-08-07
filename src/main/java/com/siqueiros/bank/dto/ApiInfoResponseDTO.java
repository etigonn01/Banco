package com.siqueiros.bank.dto;

import java.util.List;

public record ApiInfoResponse(String greeting, String version, String author, List<String> technologies) {
}
