package com.design_pattern.sidecar_pattern.transaction.orchestrator;

import com.design_pattern.sidecar_pattern.transaction.dto.Approval;
import com.design_pattern.sidecar_pattern.transaction.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

@Component
public class ApprovalOrchestrator {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalOrchestrator(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @Description(
                    "approvalService.processApproval(approval)을 호출하여 승인 프로세스를 시작한다.\n" +
                    "Approval 객체를 받아서 승인 프로세스를 초기화한다.\n" +
                    "for 루프를 사용하여 각 단계를 순차적으로 승인한다.\n" +
                    "approvalService.getApprovalSteps().size()를 호출하여 총 승인 단계 수를 가져온다.\n" +
                    "각 단계마다 approvalService.approveStep(approval.getId(), step)을 호출하여 해당 단계를 승인한다.\n" +
                    "승인 과정 중 예외가 발생하면 catch 블록이 실행된다.\n" +
                    "compensateApproval(approval.getId())을 호출하여 보상 트랜잭션을 수행한다.\n" +
                    "보상 트랜잭션은 이전에 성공한 단계를 롤백한다.\n" +
                    "예외를 다시 던져서 호출자에게 예외가 발생했음을 알린다.")
    public void executeApprovalSaga(Approval approval) {
        try {
            approvalService.processApproval(approval);
            for (int step = 1; step <= approvalService.getApprovalSteps().size(); step++) { // step이 총 승인 단계 수보다 작거나 같을 때까지 실행 된다.(승인 단계가 끝날때까지 실행)
                approvalService.approveStep(approval.getId(), step);
            } // {} 블록이 끝나면 승인 프로세스가 완료된다. {} 블록은 하나의 작업이다.
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
        // approval table에 insert, 스텝을 시작전에 insert 하는 이유는 스텝이 끝나고 업데이트 하기 위함.
        Approval approval = new Approval();
        approval.setId(approvalId);
        approval.setStatus("PENDING");
        approval.setCurrentStep("FINAL");
        approvalService.insertApproval(approval);
        // 결제 스텝 시작
        approvalService.approveStep(approvalId, step);
    }

    public void rejectStep(String approvalId, int step) {
        approvalService.rejectStep(approvalId, step);
    }
}
