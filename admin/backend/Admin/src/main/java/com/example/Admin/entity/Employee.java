package com.example.Admin.entity;

import com.example.Admin.entity.enums.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
    name = "employees",
    indexes = {
        @Index(
            name = "idx_employee_employee_id",
            columnList = "employee_id",
            unique = true
        ),
        @Index(
            name = "idx_employee_poc_id",
            columnList = "poc_id"
        ),
        @Index(
            name = "idx_employee_manager_id",
            columnList = "manager_id"
        )
    }
)
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    private String name;

    private String jobTitle;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Direct POC for EMPLOYEE only.
     * NULL for POC and MANAGER.
     */
    @Column(name = "poc_id")
    private UUID pocId;

    /**
     * Direct MANAGER for POC,
     * Derived MANAGER for EMPLOYEE,
     * NULL for MANAGER.
     */
    @Column(name = "manager_id")
    private UUID managerId;

    /**
     * Used later for signup/login.
     * Null until user registers.
     */
    private String password;

    @Column(nullable = false)
    private boolean active = true;

    /* =====================
       GETTERS
       ===================== */

    public UUID getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public Role getRole() {
        return role;
    }

    public UUID getPocId() {
        return pocId;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    /* =====================
       SETTERS
       ===================== */

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPocId(UUID pocId) {
        this.pocId = pocId;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
