package com.example.Admin.dto;

import com.example.Admin.entity.enums.Role;

import java.util.UUID;

public class AuthResponse {

    private UUID id;
    private String employeeId;
    private String name;
    private Role role;
    private UUID pocId;
    private UUID managerId;

    public AuthResponse(
            UUID id,
            String employeeId,
            String name,
            Role role,
            UUID pocId,
            UUID managerId
    ) {
        this.id = id;
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.pocId = pocId;
        this.managerId = managerId;
    }

    public UUID getId() { return id; }
    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public Role getRole() { return role; }
    public UUID getPocId() { return pocId; }
    public UUID getManagerId() { return managerId; }
}
