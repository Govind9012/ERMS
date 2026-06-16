package com.unified.auth.service;

import com.unified.auth.dto.ExcelMetaDataDTO;
import com.unified.auth.entity.Employee;
import com.unified.auth.security.PasswordUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelParserService {

    private final DataFormatter dataFormatter = new DataFormatter();

    public List<Employee> parse(
            MultipartFile file,
            ExcelMetaDataDTO meta
    ) throws Exception {

        List<Employee> employees = new ArrayList<>();
        int skippedRows = 0;

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    skippedRows++;
                    continue;
                }

                String employeeId = getStringValue(row.getCell(0));
                String name = getStringValue(row.getCell(1));

                // Skip rows with missing required fields
                if (employeeId == null || employeeId.isBlank() || 
                    name == null || name.isBlank()) {
                    skippedRows++;
                    continue;
                }

                Employee e = new Employee();
                e.setEmployeeId(employeeId.trim());
                e.setName(name.trim());
                e.setJobTitle(getStringValue(row.getCell(2)));
                e.setPoc(getStringValue(row.getCell(3)));
                e.setLocation(getStringValue(row.getCell(4)));

                String rawPassword = getStringValue(row.getCell(5));
                if (rawPassword != null && !rawPassword.isBlank()) {
                    e.setPasswordHash(PasswordUtil.hash(rawPassword.trim()));
                }

                e.setUploadedBy(meta.getUploadedBy());
                e.setUploadedAt(LocalDateTime.now());

                employees.add(e);
            }
        }

        meta.setTotalRecords(employees.size());
        meta.setSkippedRows(skippedRows);
        return employees;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        // Use DataFormatter to handle all cell types properly
        String value = dataFormatter.formatCellValue(cell);
        
        // Return null for empty strings, otherwise return trimmed value
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        
        return value.trim();
    }
}
