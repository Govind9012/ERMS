package com.unified.auth.controller;

import com.unified.auth.entity.Employee;
import com.unified.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> body) {
        authService.signup(
                body.get("employeeId"),
                body.get("name"),
                body.get("password")
        );
        return ResponseEntity.ok("Signup successful");
    }

    @PostMapping("/login")
    public ResponseEntity<Employee> login(@RequestBody Map<String, String> body) {
        Employee emp = authService.login(
                body.get("employeeId"),
                body.get("password")
        );
        emp.setPasswordHash(null);
        return ResponseEntity.ok(emp);
    }
}
