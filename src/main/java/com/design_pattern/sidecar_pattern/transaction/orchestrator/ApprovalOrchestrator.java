package com.design_pattern.sidecar_pattern.transaction.orchestrator;

import com.design_pattern.sidecar_pattern.transaction.dto.Approval;
import com.design_pattern.sidecar_pattern.transaction.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApprovalOrchestrator {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalOrchestrator(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    public void executeApprovalSaga(Approval approval) {
        try {
            approvalService.processApproval(approval);
            for (int step = 1; step <= approvalService.getApprovalSteps().size(); step++) {
                approvalService.approveStep(approval.getId(), step);
            }
        } catch (Exception ex) {
            compensateApproval(approval.getId());
            throw ex;
        }
    }

    public void compensateApproval(String approvalId) {
        for (int step = approvalService.getApprovalSteps().size(); step > 0; step--) {
            approvalService.rejectStep(approvalId, step);
        }
        approvalService.compensateApproval(approvalId);
    }

    public void approveStep(String approvalId, int step) {
        Approval approval = new Approval();
        approval.setId(approvalId);
        approval.setStatus("PENDING");
        approval.setCurrentStep("FINAL");
        approvalService.insertApproval(approval);

        approvalService.approveStep(approvalId, step);
    }

    public void rejectStep(String approvalId, int step) {
        approvalService.rejectStep(approvalId, step);
    }
}
