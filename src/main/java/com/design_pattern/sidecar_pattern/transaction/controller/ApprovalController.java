package com.design_pattern.sidecar_pattern.transaction.controller;

import com.design_pattern.sidecar_pattern.transaction.dto.Approval;
import com.design_pattern.sidecar_pattern.transaction.orchestrator.ApprovalOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approvals")
public class ApprovalController {

    private final ApprovalOrchestrator approvalOrchestrator;

    @Autowired
    public ApprovalController(ApprovalOrchestrator approvalOrchestrator) {
        this.approvalOrchestrator = approvalOrchestrator;
    }

    @PostMapping
    public ResponseEntity<String> createApproval(@RequestBody Approval approval) {
        try {
            approvalOrchestrator.executeApprovalSaga(approval);
            return ResponseEntity.ok("Approval process completed successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Approval process failed: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}/compensate")
    public ResponseEntity<String> compensateApproval(@PathVariable String id) {
        try {
            approvalOrchestrator.compensateApproval(id);
            return ResponseEntity.ok("Approval process compensated successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Compensation process failed: " + ex.getMessage());
        }
    }

    @PostMapping("/{id}/approve-step")
    public ResponseEntity<String> approveStep(@PathVariable String id, @RequestParam int step) {
        try {
            approvalOrchestrator.approveStep(id, step);
            return ResponseEntity.ok("Step " + step + " approved successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Step approval failed: " + ex.getMessage());
        }
    }

    @PostMapping("/{id}/reject-step")
    public ResponseEntity<String> rejectStep(@PathVariable String id, @RequestParam int step) {
        try {
            approvalOrchestrator.rejectStep(id, step);
            return ResponseEntity.ok("Step " + step + " rejected successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Step rejection failed: " + ex.getMessage());
        }
    }
}
