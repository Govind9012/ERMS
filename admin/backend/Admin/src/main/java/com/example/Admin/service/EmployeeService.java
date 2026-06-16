package com.example.Admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Admin.entity.Employee;
import com.example.Admin.entity.enums.Role;
import com.example.Admin.repository.EmployeeRepository;
import com.example.Admin.dto.EmployeeExportDto;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    // =========================
    // GET ALL EMPLOYEES
    // =========================
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // =========================
    // GET BY EMPLOYEE ID
    // =========================
    public Employee getEmployeeByEmployeeId(String employeeId) {
        return repository.findByEmployeeId(employeeId)
                .orElse(null);
    }

    // =========================
    // GET EMPLOYEES BY POC EMPLOYEE ID
    // =========================
    public List<Employee> getEmployeesByPocEmployeeId(String pocEmployeeId) {

        Employee poc = repository.findByEmployeeId(pocEmployeeId)
                .orElseThrow(() -> new RuntimeException("POC not found"));

        return repository.findByPocId(poc.getId());
    }

    // =========================
    // UPDATE EMPLOYEE
    // =========================
    public Employee updateEmployee(String employeeId, Employee updatedEmp) {

        Employee existing = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        existing.setName(updatedEmp.getName());
        existing.setJobTitle(updatedEmp.getJobTitle());
        existing.setLocation(updatedEmp.getLocation());

        return repository.save(existing);
    }

    // =========================
    // DELETE EMPLOYEE
    // =========================
    public void deleteEmployee(String employeeId) {

        Employee emp = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        repository.delete(emp);
    }

    // =========================
    // ADD EMPLOYEE (MANUAL)
    // =========================
    public Employee addEmployee(Employee employee, String pocEmployeeId) {

        if (repository.existsByEmployeeId(employee.getEmployeeId())) {
            throw new RuntimeException(
                    "Employee already exists with id: " + employee.getEmployeeId());
        }

        employee.setActive(true);

        // =========================
        // ROLE & HIERARCHY LOGIC
        // =========================

        if (pocEmployeeId == null || pocEmployeeId.isBlank()) {
            // No POC selected → this is a MANAGER
            employee.setRole(Role.MANAGER);
            employee.setPocId(null);
            employee.setManagerId(null);

        } else {
            Employee poc = repository.findByEmployeeId(pocEmployeeId)
                    .orElseThrow(() -> new RuntimeException("POC not found"));

            employee.setPocId(poc.getId());

            if (poc.getRole() == Role.MANAGER) {
                // Reporting to Manager → becomes POC
                employee.setRole(Role.POC);
                employee.setManagerId(poc.getId());
            } else {
                // Reporting to POC → normal Employee
                employee.setRole(Role.EMPLOYEE);

                if (poc.getManagerId() != null) {
                    employee.setManagerId(poc.getManagerId());
                } else {
                    employee.setManagerId(poc.getId());
                }
            }
        }

        return repository.save(employee);
    }

    // get POC and Manaager

    public List<Employee> getAssignablePocs() {
        return repository.findByRoleIn(
                List.of(Role.POC, Role.MANAGER));
    }

    // Method for Export the excel with the same Format

    public List<EmployeeExportDto> getEmployeesForExport() {

        return repository.findAll()
                .stream()
                .map(emp -> {

                    String pocName = null;

                    if (emp.getPocId() != null) {
                        Optional<Employee> pocOpt = repository.findById(emp.getPocId());

                        pocName = pocOpt
                                .map(Employee::getName)
                                .orElse(null);
                    }

                    return new EmployeeExportDto(
                            emp.getEmployeeId(),
                            emp.getName(),
                            emp.getJobTitle(),
                            pocName,
                            emp.getLocation());
                })
                .collect(Collectors.toList());
    }

}
