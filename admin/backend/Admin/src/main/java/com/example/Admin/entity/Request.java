package com.example.Admin.entity;

import com.example.Admin.entity.enums.RequestStage;
import com.example.Admin.entity.enums.RequestStatus;
import com.example.Admin.entity.enums.RequestType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
    name = "requests",
    indexes = {
        @Index(name = "idx_request_employee", columnList = "employeeId"),
        @Index(name = "idx_request_poc_stage", columnList = "pocId, stage"),
        @Index(name = "idx_request_manager_stage", columnList = "managerId, stage")
    }
)
public class Request extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStage stage;

    @Column(nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private UUID pocId;

    @Column(nullable = false)
    private UUID managerId;

    @Column(nullable = false)
    private boolean deleted = false;

    public UUID getId() {
        return id;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public RequestStage getStage() {
        return stage;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public UUID getPocId() {
        return pocId;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setStage(RequestStage stage) {
        this.stage = stage;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public void setPocId(UUID pocId) {
        this.pocId = pocId;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
