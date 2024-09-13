package com.design_pattern.sidecar_pattern.transaction.step.approval;

import com.design_pattern.sidecar_pattern.transaction.dto.Approval;

public interface ApprovalStep {
    void execute(Approval approval);
}
