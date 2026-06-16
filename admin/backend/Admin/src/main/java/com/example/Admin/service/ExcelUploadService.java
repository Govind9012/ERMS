package com.example.Admin.service;

import com.example.Admin.entity.Employee;
import com.example.Admin.entity.enums.Role;
import com.example.Admin.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Service
public class ExcelUploadService {

    @Autowired
    private EmployeeRepository repository;

    /**
     * Excel Format (UNCHANGED):
     * 0 -> Employee ID
     * 1 -> Name
     * 2 -> Job Title
     * 3 -> Reports To (POC / Manager / Director)
     * 4 -> Location
     */
    public void saveExcel(MultipartFile file) throws Exception {

        // employeeId -> reportsToName
        Map<String, String> relations = new HashMap<>();

        try (InputStream is = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            // ============================
            // PASS 1: SAVE ALL EMPLOYEES
            // ============================
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                String employeeId = getCellValue(row.getCell(0));
                if (employeeId.isBlank())
                    continue;

                Employee emp = repository.findByEmployeeId(employeeId)
                        .orElse(new Employee());

                emp.setEmployeeId(employeeId);
                emp.setName(getCellValue(row.getCell(1)));
                emp.setJobTitle(getCellValue(row.getCell(2)));
                emp.setLocation(getCellValue(row.getCell(4)));
                emp.setActive(true);

                // Temporary role to satisfy NOT NULL constraint
                emp.setRole(Role.EMPLOYEE);

                repository.save(emp);

                String reportsTo = getCellValue(row.getCell(3));
                if (!reportsTo.isBlank()) {
                    relations.put(employeeId, reportsTo.trim());
                }
            }

            // ============================
            // PASS 2: RESOLVE HIERARCHY
            // (IGNORE MISSING SUPERVISORS)
            // ============================
            for (Map.Entry<String, String> entry : relations.entrySet()) {

                Employee employee = repository.findByEmployeeId(entry.getKey())
                        .orElse(null);

                if (employee == null)
                    continue;

                String reportsToName = entry.getValue();

                Optional<Employee> supervisorOpt = repository.findByNameIgnoreCase(reportsToName);

                // 🚫 Supervisor not found → IGNORE relationship
                if (supervisorOpt.isEmpty()) {
                employee.setPocId(null);
                employee.setManagerId(null);
                repository.save(employee);
                continue;
                }
                // if (supervisorOpt.isEmpty()) {
                //     // supervisor not found → ignore relationship, DO NOT RESET hierarchy
                //     continue;
                // }

                Employee supervisor = supervisorOpt.get();

                employee.setPocId(supervisor.getId());

                // Inherit top-level manager
                if (supervisor.getManagerId() != null) {
                    employee.setManagerId(supervisor.getManagerId());
                } else {
                    employee.setManagerId(supervisor.getId());
                }

                repository.save(employee);
            }

            // ============================
            // PASS 3: DERIVE ROLES
            // ============================
            List<Employee> allEmployees = repository.findAll();

            for (Employee emp : allEmployees) {

                boolean hasSupervisor = emp.getPocId() != null;
                boolean hasReports = repository.existsByPocId(emp.getId());

                if (!hasSupervisor) {
                    emp.setRole(Role.MANAGER);
                } else if (hasReports) {
                    emp.setRole(Role.POC);
                } else {
                    emp.setRole(Role.EMPLOYEE);
                }

                repository.save(emp);
            }
        }
    }

    // ============================
    // SAFE CELL VALUE READER
    // ============================
    private String getCellValue(Cell cell) {
        if (cell == null)
            return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}
