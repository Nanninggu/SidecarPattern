package com.design_pattern.sidecar_pattern.transaction.orchestrator;

import com.design_pattern.sidecar_pattern.transaction.dto.Payment;
import com.design_pattern.sidecar_pattern.transaction.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentSagaOrchestrator {
    private final PaymentService paymentService;

    @Autowired
    public PaymentSagaOrchestrator(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void executePaymentSaga(Payment payment) {
        try {
            paymentService.processPayment(payment);
        } catch (Exception ex) {
            paymentService.compensatePayment(payment.getId());
            throw ex; // throw로 예외를 다시 던지면, PaymentController에서 catch하여 처리할 수 있음.
        }
    }
}
