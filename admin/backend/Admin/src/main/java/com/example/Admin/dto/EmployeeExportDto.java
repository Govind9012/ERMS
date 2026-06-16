package com.example.Admin.dto;

public class EmployeeExportDto {

    private String employeeId;
    private String name;
    private String jobTitle;
    private String poc;
    private String location;

    public EmployeeExportDto(
            String employeeId,
            String name,
            String jobTitle,
            String poc,
            String location) {
        this.employeeId = employeeId;
        this.name = name;
        this.jobTitle = jobTitle;
        this.poc = poc;
        this.location = location;
    }

    public String getEmployeeId() { return employeeId; }
    public String getName() { return name; }
    public String getJobTitle() { return jobTitle; }
    public String getPoc() { return poc; }
    public String getLocation() { return location; }
}
