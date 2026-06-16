package com.example.Admin.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Admin.service.ExcelUploadService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/excel")
public class ExcelUploadController {

    @Autowired
    private ExcelUploadService service;

    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            service.saveExcel(file);
            return "Excel uploaded & data saved successfully";
        } catch (Exception e) {
            return "Upload failed: " + e.getMessage();
        }
    }
}
