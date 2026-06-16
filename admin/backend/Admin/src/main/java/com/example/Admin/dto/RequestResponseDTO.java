package com.example.Admin.dto;

import com.example.Admin.entity.enums.RequestStage;
import com.example.Admin.entity.enums.RequestStatus;
import com.example.Admin.entity.enums.RequestType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class RequestResponseDTO {

    private UUID requestId;
    private RequestType requestType;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private RequestStatus status;
    private RequestStage stage;
    private LocalDateTime createdAt;

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestStage getStage() {
        return stage;
    }

    public void setStage(RequestStage stage) {
        this.stage = stage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
