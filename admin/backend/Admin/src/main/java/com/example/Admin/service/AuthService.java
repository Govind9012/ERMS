package com.example.Admin.service;

import com.example.Admin.dto.AuthRequest;
import com.example.Admin.dto.AuthResponse;
import com.example.Admin.dto.SignupRequest;
import com.example.Admin.entity.Employee;
import com.example.Admin.repository.EmployeeRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository repository;
    private final BCryptPasswordEncoder encoder;

    public AuthService(EmployeeRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    // ======================
    // LOGIN
    // ======================
    public AuthResponse login(AuthRequest request) {

        Employee employee = repository
                .findByEmployeeIdIgnoreCase(request.getEmployeeIdOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (employee.getPassword() == null) {
            throw new RuntimeException("Account not activated. Please signup.");
        }

        if (!encoder.matches(request.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return map(employee);
    }

    // ======================
    // SIGNUP
    // ======================
    public void signup(SignupRequest request) {

        Employee employee = repository
                .findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getPassword() != null) {
            throw new RuntimeException("Account already activated");
        }

        employee.setPassword(encoder.encode(request.getPassword()));
        repository.save(employee);
    }

    private AuthResponse map(Employee e) {
        return new AuthResponse(
                e.getId(),
                e.getEmployeeId(),
                e.getName(),
                e.getRole(),
                e.getPocId(),
                e.getManagerId());
    }
}
