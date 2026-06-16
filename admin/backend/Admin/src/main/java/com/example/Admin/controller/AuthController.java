package com.example.Admin.controller;

import com.example.Admin.dto.AuthRequest;
import com.example.Admin.dto.AuthResponse;
import com.example.Admin.dto.SignupRequest;
import com.example.Admin.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok().build();
    }
}
