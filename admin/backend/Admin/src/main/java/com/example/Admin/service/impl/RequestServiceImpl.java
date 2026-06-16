package com.example.Admin.service.impl;

import com.example.Admin.dto.CreateRequestDTO;
import com.example.Admin.dto.RequestResponseDTO;
import com.example.Admin.entity.Employee;
import com.example.Admin.entity.Request;
import com.example.Admin.entity.enums.RequestStage;
import com.example.Admin.entity.enums.RequestStatus;
import com.example.Admin.repository.EmployeeRepository;
import com.example.Admin.repository.RequestRepository;
import com.example.Admin.service.RequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    private final EmployeeRepository employeeRepository;
    private final RequestRepository requestRepository;

    public RequestServiceImpl(
            EmployeeRepository employeeRepository,
            RequestRepository requestRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public RequestResponseDTO createRequest(String employeeId, CreateRequestDTO dto) {

        UUID empId = UUID.fromString(employeeId);

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getPocId() == null || employee.getManagerId() == null) {
            throw new RuntimeException("POC or Manager not assigned");
        }

        Request request = new Request();
        request.setRequestType(dto.getRequestType());
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());

        request.setEmployeeId(employee.getId());
        request.setPocId(employee.getPocId());
        request.setManagerId(employee.getManagerId());

        // Initial workflow state
        request.setStatus(RequestStatus.PENDING);
        request.setStage(RequestStage.POC_PENDING);

        return mapToDto(requestRepository.save(request));
    }

    @Override
    public List<RequestResponseDTO> getMyRequests(String employeeId) {

        UUID empId = UUID.fromString(employeeId);

        return requestRepository.findByEmployeeId(empId)
                .stream()
                .filter(r -> !r.isDeleted())
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelRequest(String employeeId, UUID requestId) {

        UUID empId = UUID.fromString(employeeId);

        Request request = requestRepository
                .findByIdAndEmployeeId(requestId, empId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Cancel allowed only before POC approval
        if (request.getStage() != RequestStage.POC_PENDING) {
            throw new RuntimeException("Request cannot be cancelled at this stage");
        }

        request.setStatus(RequestStatus.CANCELLED);
        request.setStage(RequestStage.REJECTED_POC);
        requestRepository.save(request);
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
