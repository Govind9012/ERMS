package com.example.Admin.service.impl;

import com.example.Admin.dto.ApprovalActionDTO;
import com.example.Admin.dto.RequestResponseDTO;
import com.example.Admin.entity.ApprovalHistory;
import com.example.Admin.entity.Employee;
import com.example.Admin.entity.Request;
import com.example.Admin.entity.enums.ApprovalAction;
import com.example.Admin.entity.enums.RequestStage;
import com.example.Admin.entity.enums.RequestStatus;
import com.example.Admin.entity.enums.Role;
import com.example.Admin.repository.ApprovalHistoryRepository;
import com.example.Admin.repository.EmployeeRepository;
import com.example.Admin.repository.RequestRepository;
import com.example.Admin.service.ApprovalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    private final EmployeeRepository employeeRepository;
    private final RequestRepository requestRepository;
    private final ApprovalHistoryRepository approvalHistoryRepository;

    public ApprovalServiceImpl(
            EmployeeRepository employeeRepository,
            RequestRepository requestRepository,
            ApprovalHistoryRepository approvalHistoryRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.requestRepository = requestRepository;
        this.approvalHistoryRepository = approvalHistoryRepository;
    }

    /* =========================
       PENDING LISTS
       ========================= */

    @Override
    public List<RequestResponseDTO> getPendingForPoc(String employeeId) {
        UUID pocId = UUID.fromString(employeeId);

        employeeRepository.findById(pocId)
                .orElseThrow(() -> new RuntimeException("POC not found"));

        return requestRepository
                .findByPocIdAndStage(pocId, RequestStage.POC_PENDING)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestResponseDTO> getPendingForManager(String employeeId) {
        UUID managerId = UUID.fromString(employeeId);

        employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        return requestRepository
                .findByManagerIdAndStage(managerId, RequestStage.MANAGER_PENDING)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /* =========================
       POC ACTIONS
       ========================= */

    @Override
    public void approveByPoc(String employeeId, UUID requestId, ApprovalActionDTO dto) {

        UUID pocId = UUID.fromString(employeeId);

        Employee poc = employeeRepository.findById(pocId)
                .orElseThrow(() -> new RuntimeException("POC not found"));

        Request request = requestRepository
                .findByIdAndPocId(requestId, poc.getId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStage() != RequestStage.POC_PENDING) {
            throw new RuntimeException("Request is not pending with POC");
        }

        request.setStage(RequestStage.MANAGER_PENDING);
        request.setStatus(RequestStatus.PENDING);
        requestRepository.save(request);

        saveHistory(request, poc.getId(), Role.POC, ApprovalAction.APPROVED, dto.getRemark());
    }

    @Override
    public void rejectByPoc(String employeeId, UUID requestId, ApprovalActionDTO dto) {

        UUID pocId = UUID.fromString(employeeId);

        Employee poc = employeeRepository.findById(pocId)
                .orElseThrow(() -> new RuntimeException("POC not found"));

        Request request = requestRepository
                .findByIdAndPocId(requestId, poc.getId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStage() != RequestStage.POC_PENDING) {
            throw new RuntimeException("Request is not pending with POC");
        }

        request.setStage(RequestStage.REJECTED_POC);
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);

        saveHistory(request, poc.getId(), Role.POC, ApprovalAction.REJECTED, dto.getRemark());
    }

    /* =========================
       MANAGER ACTIONS
       ========================= */

    @Override
    public void approveByManager(String employeeId, UUID requestId, ApprovalActionDTO dto) {

        UUID managerId = UUID.fromString(employeeId);

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        Request request = requestRepository
                .findByIdAndManagerId(requestId, manager.getId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStage() != RequestStage.MANAGER_PENDING) {
            throw new RuntimeException("Request is not pending with Manager");
        }

        request.setStage(RequestStage.APPROVED);
        request.setStatus(RequestStatus.APPROVED);
        requestRepository.save(request);

        saveHistory(request, manager.getId(), Role.MANAGER, ApprovalAction.APPROVED, dto.getRemark());
    }

    @Override
    public void rejectByManager(String employeeId, UUID requestId, ApprovalActionDTO dto) {

        UUID managerId = UUID.fromString(employeeId);

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        Request request = requestRepository
                .findByIdAndManagerId(requestId, manager.getId())
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getStage() != RequestStage.MANAGER_PENDING) {
            throw new RuntimeException("Request is not pending with Manager");
        }

        request.setStage(RequestStage.REJECTED_MANAGER);
        request.setStatus(RequestStatus.REJECTED);
        requestRepository.save(request);

        saveHistory(request, manager.getId(), Role.MANAGER, ApprovalAction.REJECTED, dto.getRemark());
    }

    /* =========================
       HELPERS
       ========================= */

    private void saveHistory(
            Request request,
            UUID actionBy,
            Role role,
            ApprovalAction action,
            String remark
    ) {
        ApprovalHistory history = new ApprovalHistory();
        history.setRequestId(request.getId());
        history.setActionBy(actionBy);
        history.setRole(role);
        history.setAction(action);
        history.setRemark(remark);

        approvalHistoryRepository.save(history);
    }

    private RequestResponseDTO mapToDto(Request request) {
        RequestResponseDTO dto = new RequestResponseDTO();
        dto.setRequestId(request.getId());
        dto.setRequestType(request.getRequestType());
        dto.setTitle(request.getTitle());
        dto.setDescription(request.getDescription());
        dto.setStartDate(request.getStartDate());
        dto.setEndDate(request.getEndDate());
        dto.setStatus(request.getStatus());
        dto.setStage(request.getStage());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }
}
