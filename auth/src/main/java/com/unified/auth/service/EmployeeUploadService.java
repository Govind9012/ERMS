package com.unified.auth.service;

import com.unified.auth.dto.ExcelMetaDataDTO;
import com.unified.auth.entity.Employee;
import com.unified.auth.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EmployeeUploadService {

    private final EmployeeRepository repository;
    private final ExcelParserService parser;

    public EmployeeUploadService(
            EmployeeRepository repository,
            ExcelParserService parser
    ) {
        this.repository = repository;
        this.parser = parser;
    }

    @Transactional
    public ExcelMetaDataDTO upload(
            MultipartFile file,
            String uploadedBy
    ) throws Exception {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        ExcelMetaDataDTO meta = new ExcelMetaDataDTO();
        meta.setFileName(file.getOriginalFilename());
        meta.setUploadedBy(uploadedBy);

        List<Employee> employees = parser.parse(file, meta);
        
        if (employees.isEmpty()) {
            return meta;
        }

        // Handle duplicates: check for existing employeeIds and duplicates within the batch
        Set<String> seenEmployeeIds = new HashSet<>();
        List<Employee> validEmployees = new ArrayList<>();
        int duplicateCount = 0;

        for (Employee employee : employees) {
            String employeeId = employee.getEmployeeId();
            
            // Check for duplicates within the current batch
            if (seenEmployeeIds.contains(employeeId)) {
                duplicateCount++;
                continue;
            }
            
            // Check if employee already exists in database
            if (repository.findByEmployeeId(employeeId).isPresent()) {
                duplicateCount++;
                continue;
            }
            
            seenEmployeeIds.add(employeeId);
            validEmployees.add(employee);
        }

        meta.setDuplicateRows(duplicateCount);

        // Save only valid employees
        if (!validEmployees.isEmpty()) {
            try {
                repository.saveAll(validEmployees);
                meta.setTotalRecords(validEmployees.size());
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Failed to save employees. Possible duplicate employee IDs or constraint violation: " + e.getMessage(), e);
            }
        }

        return meta;
    }
}
