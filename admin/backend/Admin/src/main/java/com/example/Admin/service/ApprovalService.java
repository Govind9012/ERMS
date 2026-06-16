package com.example.Admin.service;

import com.example.Admin.dto.ApprovalActionDTO;
import com.example.Admin.dto.RequestResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ApprovalService {

    List<RequestResponseDTO> getPendingForPoc(String employeeId);

    List<RequestResponseDTO> getPendingForManager(String employeeId);

    void approveByPoc(String employeeId, UUID requestId, ApprovalActionDTO dto);

    void rejectByPoc(String employeeId, UUID requestId, ApprovalActionDTO dto);

    void approveByManager(String employeeId, UUID requestId, ApprovalActionDTO dto);

    void rejectByManager(String employeeId, UUID requestId, ApprovalActionDTO dto);
}
