package com.example.Admin.dto;

public class AuthRequest {
    private String employeeIdOrEmail;
    private String password;

    public String getEmployeeIdOrEmail() {
        return employeeIdOrEmail;
    }

    public void setEmployeeIdOrEmail(String employeeIdOrEmail) {
        this.employeeIdOrEmail = employeeIdOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
