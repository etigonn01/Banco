package com.siqueiros.bank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.siqueiros.bank.dto.ApiInfoResponse;

@RestController
@RequestMapping("/api/v1/info")
public class ApiInforController {
    @GetMapping()
    public ResponseEntity<ApiInfoResponse> getInfo() {
        String greeting = "Welcome to my Bank API";
        String version = "1.0.0";
        String author = "Pedro Siqueiros";
        List<String> technologies = List.of("Java SE 21", "PostgreSQL 15", "Spring Boot");

        ApiInfoResponse response = new ApiInfoResponse(
                greeting,
                version,
                author,
                technologies
        );
        return ResponseEntity.ok(response);
    }
}
