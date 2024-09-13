package com.design_pattern.sidecar_pattern.transaction.service;

import com.design_pattern.sidecar_pattern.transaction.dto.Approval;
import com.design_pattern.sidecar_pattern.transaction.mapper.ApprovalMapper;
import com.design_pattern.sidecar_pattern.transaction.step.approval.ApprovalStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApprovalService {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalService.class);
    private final ApprovalMapper approvalMapper;
    private final List<ApprovalStep> approvalSteps;

    @Autowired
    public ApprovalService(ApprovalMapper approvalMapper, List<ApprovalStep> approvalSteps) {
        this.approvalMapper = approvalMapper;
        this.approvalSteps = approvalSteps;
    }

    public List<ApprovalStep> getApprovalSteps() {
        return approvalSteps;
    }

    @Transactional
    public void processApproval(Approval approval) {
        approval.setStatus("PENDING");
        approvalMapper.insertApproval(approval);

        for (ApprovalStep step : approvalSteps) {
            step.execute(approval);
        }
    }

    @Transactional
    public void insertApproval(Approval approval) {
        approvalMapper.insertApproval(approval);
    }

    @Transactional
    public void compensateApproval(String approvalId) {
        for (int step = approvalSteps.size(); step > 0; step--) {
            String stepStatus = getStepStatus(step);
            approvalMapper.insertStepStatus(approvalId, stepStatus, "ROLLED_BACK");
            logger.info("Step {} rolled back for approval: {}", step, approvalId);
        }
        approvalMapper.updateApprovalStatus(approvalId, "CANCELLED");
        logger.info("Approval status updated to CANCELLED for approval: {}", approvalId);
    }

    @Transactional
    public void approveStep(String approvalId, int step) {
        try {
            String stepStatus = getStepStatus(step);
            approvalMapper.insertStepStatus(approvalId, stepStatus, "APPROVED");
            logger.info("Step {} approved for approval: {}", step, approvalId);

            if (step == approvalSteps.size()) {
                logger.debug("All steps completed. Updating approval status to COMPLETED for approval: {}", approvalId);
                approvalMapper.updateApprovalStatus(approvalId, "COMPLETED");
                logger.info("Approval status updated to COMPLETED for approval: {}", approvalId);

//                // Insert the approval record (완료 됐을때 approvals 테이블에 insert)
//                Approval approval = new Approval();
//                approval.setId(approvalId);
//                approval.setStatus("COMPLETED");
//                approval.setCurrentStep("APPROVED");
//                approvalMapper.insertApproval(approval); // This line ensures the approval record is inserted
            } else {
                logger.debug("Current step: {}. Total steps: {}", step, approvalSteps.size());
            }
        } catch (Exception e) {
            logger.error("Error approving step {} for approval {}: {}", step, approvalId, e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void rejectStep(String approvalId, int step) {
        String stepStatus = getStepStatus(step);
        approvalMapper.insertStepStatus(approvalId, stepStatus, "REJECTED");
        approvalMapper.updateApprovalStatus(approvalId, "REJECTED");
        logger.info("Step {} rejected for approval: {}", step, approvalId);

        // Compensate previous steps, 보상 트랜잭션
        compensateApproval(approvalId);
    }

    private String getStepStatus(int step) {
        switch (step) {
            case 1:
                return "INITIAL";
            case 2:
                return "GROUP_LEADER";
            case 3:
                return "TEAM_LEADER";
            case 4:
                return "FINAL";
            default:
                throw new IllegalArgumentException("Invalid step: " + step);
        }
    }
}
