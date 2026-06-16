package com.example.Admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Admin.entity.Employee;
import com.example.Admin.service.EmployeeService;
import com.example.Admin.dto.EmployeeExportDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public Employee addEmployee(
            @RequestBody Employee employee,
            @RequestParam(required = false) String pocEmployeeId) {

        return service.addEmployee(employee, pocEmployeeId);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return service.getAllEmployees();
    }

    @GetMapping("/{employeeId}")
    public Employee getEmployeeById(@PathVariable String employeeId) {
        return service.getEmployeeByEmployeeId(employeeId);
    }

    @GetMapping("/poc/{pocEmployeeId}")
    public List<Employee> getEmployeesByPoc(@PathVariable String pocEmployeeId) {
        return service.getEmployeesByPocEmployeeId(pocEmployeeId);
    }

    @PutMapping("/{employeeId}")
    public Employee updateEmployee(
            @PathVariable String employeeId,
            @RequestBody Employee employee) {
        return service.updateEmployee(employeeId, employee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String employeeId) {
        service.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignable-pocs")
    public List<Employee> getAssignablePocs() {
        return service.getAssignablePocs();
    }

    // Export

    @GetMapping("/export")
    public ResponseEntity<String> exportEmployeesCsv() {

        List<EmployeeExportDto> data = service.getEmployeesForExport();

        StringBuilder csv = new StringBuilder();
        csv.append("Employee ID,Name,Job Title,POC,Location\n");

        for (EmployeeExportDto e : data) {
            csv.append(String.format(
                    "%s,%s,%s,%s,%s\n",
                    e.getEmployeeId(),
                    e.getName(),
                    e.getJobTitle(),
                    e.getPoc(),
                    e.getLocation()));
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=teams-export.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csv.toString());
    }

}
