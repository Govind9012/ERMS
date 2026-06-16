package com.example.Admin.repository;

import com.example.Admin.entity.Request;
import com.example.Admin.entity.enums.RequestStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {

    List<Request> findByEmployeeId(UUID employeeId);

    List<Request> findByPocIdAndStage(UUID pocId, RequestStage stage);

    List<Request> findByManagerIdAndStage(UUID managerId, RequestStage stage);

    Optional<Request> findByIdAndEmployeeId(UUID id, UUID employeeId);

    Optional<Request> findByIdAndPocId(UUID id, UUID pocId);

    Optional<Request> findByIdAndManagerId(UUID id, UUID managerId);
}
