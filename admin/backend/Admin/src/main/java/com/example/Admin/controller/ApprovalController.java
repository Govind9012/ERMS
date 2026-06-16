package com.example.Admin.controller;

import com.example.Admin.dto.ApprovalActionDTO;
import com.example.Admin.dto.RequestResponseDTO;
import com.example.Admin.service.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @GetMapping("/poc")
    public ResponseEntity<List<RequestResponseDTO>> getPendingForPoc(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId) {

        return ResponseEntity.ok(approvalService.getPendingForPoc(employeeId));
    }

    @GetMapping("/manager")
    public ResponseEntity<List<RequestResponseDTO>> getPendingForManager(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId) {

        return ResponseEntity.ok(approvalService.getPendingForManager(employeeId));
    }

    @PostMapping("/poc/{requestId}/approve")
    public ResponseEntity<Void> approveByPoc(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId,
            @PathVariable UUID requestId,
            @RequestBody ApprovalActionDTO dto) {

        approvalService.approveByPoc(employeeId, requestId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/poc/{requestId}/reject")
    public ResponseEntity<Void> rejectByPoc(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId,
            @PathVariable UUID requestId,
            @RequestBody ApprovalActionDTO dto) {

        approvalService.rejectByPoc(employeeId, requestId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/manager/{requestId}/approve")
    public ResponseEntity<Void> approveByManager(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId,
            @PathVariable UUID requestId,
            @RequestBody ApprovalActionDTO dto) {

        approvalService.approveByManager(employeeId, requestId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/manager/{requestId}/reject")
    public ResponseEntity<Void> rejectByManager(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId,
            @PathVariable UUID requestId,
            @RequestBody ApprovalActionDTO dto) {

        approvalService.rejectByManager(employeeId, requestId, dto);
        return ResponseEntity.ok().build();
    }
}
