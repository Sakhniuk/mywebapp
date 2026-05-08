package com.example.mywebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;

@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health/alive")
    public ResponseEntity<String> alive() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/health/ready")
    public ResponseEntity<String> ready() {
        try {
            dataSource.getConnection().close();
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database connection failed: " + e.getMessage());
        }
    }
}
