package com.example.Admin.entity;

import com.example.Admin.entity.enums.ApprovalAction;
import com.example.Admin.entity.enums.Role;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "approval_history")
public class ApprovalHistory extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID requestId;

    @Column(nullable = false)
    private UUID actionBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalAction action;

    @Column(columnDefinition = "TEXT")
    private String remark;

    public UUID getId() {
        return id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public UUID getActionBy() {
        return actionBy;
    }

    public Role getRole() {
        return role;
    }

    public ApprovalAction getAction() {
        return action;
    }

    public String getRemark() {
        return remark;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public void setActionBy(UUID actionBy) {
        this.actionBy = actionBy;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAction(ApprovalAction action) {
        this.action = action;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
