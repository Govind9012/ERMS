package com.example.Admin.controller;

import com.example.Admin.dto.CreateRequestDTO;
import com.example.Admin.dto.RequestResponseDTO;
import com.example.Admin.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<RequestResponseDTO> createRequest(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId,
            @RequestBody CreateRequestDTO dto) {

        return ResponseEntity.ok(requestService.createRequest(employeeId, dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RequestResponseDTO>> getMyRequests(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId) {

        return ResponseEntity.ok(requestService.getMyRequests(employeeId));
    }

    @PostMapping("/{requestId}/cancel")
    public ResponseEntity<Void> cancelRequest(
            @RequestHeader("X-EMPLOYEE-ID") String employeeId,
            @PathVariable UUID requestId) {

        requestService.cancelRequest(employeeId, requestId);
        return ResponseEntity.ok().build();
    }
}
