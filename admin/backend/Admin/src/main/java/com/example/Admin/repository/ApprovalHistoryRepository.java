package com.example.Admin.repository;

import com.example.Admin.entity.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, UUID> {

    List<ApprovalHistory> findByRequestIdOrderByCreatedAtAsc(UUID requestId);
}
