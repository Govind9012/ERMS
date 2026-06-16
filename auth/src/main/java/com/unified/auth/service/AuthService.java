package com.unified.auth.service;

import com.unified.auth.entity.Employee;
import com.unified.auth.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(EmployeeRepository employeeRepository,
                       PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // SIGNUP = set password for existing employee
    public void signup(String employeeId, String name, String password) {
        Employee employee = employeeRepository
                .findByEmployeeId(employeeId.trim())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getPasswordHash() != null) {
            throw new RuntimeException("User already signed up");
        }

        employee.setPasswordHash(passwordEncoder.encode(password));
        employeeRepository.save(employee);
    }

    public Employee login(String employeeId, String password) {
        Employee employee = employeeRepository
                .findByEmployeeId(employeeId.trim())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getPasswordHash() == null) {
            throw new RuntimeException("User not signed up");
        }

        if (!passwordEncoder.matches(password, employee.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return employee;
    }
}
