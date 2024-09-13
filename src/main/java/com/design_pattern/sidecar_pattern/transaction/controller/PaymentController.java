package com.design_pattern.sidecar_pattern.transaction.controller;

import com.design_pattern.sidecar_pattern.transaction.dto.Payment;
import com.design_pattern.sidecar_pattern.transaction.orchestrator.PaymentSagaOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentSagaOrchestrator paymentSagaOrchestrator;

    @Autowired
    public PaymentController(PaymentSagaOrchestrator paymentSagaOrchestrator) {
        this.paymentSagaOrchestrator = paymentSagaOrchestrator;
    }

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody Payment payment) {
        try {
            paymentSagaOrchestrator.executePaymentSaga(payment);
            return new ResponseEntity<>("Payment processed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Payment processing failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
