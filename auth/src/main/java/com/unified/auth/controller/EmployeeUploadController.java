package com.unified.auth.controller;

import com.unified.auth.dto.ExcelMetaDataDTO;
import com.unified.auth.service.EmployeeUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
public class EmployeeUploadController {

    private final EmployeeUploadService service;

    public EmployeeUploadController(EmployeeUploadService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam String uploadedBy
    ) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "File is required"));
            }

            if (uploadedBy == null || uploadedBy.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "uploadedBy parameter is required"));
            }

            ExcelMetaDataDTO meta = service.upload(file, uploadedBy);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Excel uploaded successfully");
            response.put("fileName", meta.getFileName());
            response.put("uploadedBy", meta.getUploadedBy());
            response.put("totalRecords", meta.getTotalRecords());
            response.put("skippedRows", meta.getSkippedRows());
            response.put("duplicateRows", meta.getDuplicateRows());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload Excel file: " + e.getMessage()));
        }
    }
}
