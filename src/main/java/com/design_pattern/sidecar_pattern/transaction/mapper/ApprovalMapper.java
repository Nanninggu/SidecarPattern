package com.design_pattern.sidecar_pattern.transaction.mapper;

import com.design_pattern.sidecar_pattern.transaction.dto.Approval;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApprovalMapper {
    void insertApproval(Approval approval);

    void updateApprovalStatus(String id, String status);

    void insertStepStatus(String approvalId, String stepName, String status);
}
