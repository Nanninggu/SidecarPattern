package com.design_pattern.sidecar_pattern.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApprovalFlowController {

    @GetMapping("/approval-flow")
    public String getApprovalFlowPage() {
        return "approval-flow";
    }
}
