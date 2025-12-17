package com.kuspidartistmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        // You can add lightweight DB or service checks here if needed
        return ResponseEntity.ok("UP");
    }
}
