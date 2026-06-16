package com.example.Admin.service;

import com.example.Admin.dto.CreateRequestDTO;
import com.example.Admin.dto.RequestResponseDTO;

import java.util.List;
import java.util.UUID;

public interface RequestService {

    RequestResponseDTO createRequest(String employeeId, CreateRequestDTO dto);

    List<RequestResponseDTO> getMyRequests(String employeeId);

    void cancelRequest(String employeeId, UUID requestId);
}
