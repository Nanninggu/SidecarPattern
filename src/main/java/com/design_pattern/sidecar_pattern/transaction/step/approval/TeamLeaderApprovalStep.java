package com.design_pattern.sidecar_pattern.transaction.step.approval;

import com.design_pattern.sidecar_pattern.transaction.dto.Approval;
import com.design_pattern.sidecar_pattern.transaction.mapper.ApprovalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamLeaderApprovalStep implements ApprovalStep {
    private static final Logger logger = LoggerFactory.getLogger(TeamLeaderApprovalStep.class);
    private final ApprovalMapper approvalMapper;

    @Autowired
    public TeamLeaderApprovalStep(ApprovalMapper approvalMapper) {
        this.approvalMapper = approvalMapper;
    }

    @Override
    public void execute(Approval approval) {
        saveStepStatus(approval.getId(), "TEAM_LEADER", "COMPLETED");
        approval.setStatus("TEAM_LEADER_COMPLETED");
        approvalMapper.updateApprovalStatus(approval.getId(), approval.getStatus());
        logger.info("Approval status updated to TEAM_LEADER_COMPLETED for approval: {}", approval.getId());
    }

    private void saveStepStatus(String approvalId, String stepName, String status) {
        approvalMapper.insertStepStatus(approvalId, stepName, status);
        logger.info("Step status saved: approvalId={}, stepName={}, status={}", approvalId, stepName, status);
    }
}
