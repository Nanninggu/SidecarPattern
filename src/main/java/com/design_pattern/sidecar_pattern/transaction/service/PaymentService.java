package com.design_pattern.sidecar_pattern.transaction.service;

import com.design_pattern.sidecar_pattern.transaction.dto.Payment;
import com.design_pattern.sidecar_pattern.transaction.mapper.PaymentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentService(PaymentMapper paymentMapper) {
        this.paymentMapper = paymentMapper;
    }

    @Transactional
    public void processPayment(Payment payment) {
        payment.setStatus("PENDING");
        logger.info("Inserting payment: {}", payment);
        int rowsInserted = paymentMapper.insertPayment(payment);
        logger.info("Rows inserted: {}", rowsInserted);

        if (payment.getAmount() > 1000) {
            throw new RuntimeException("Payment amount too large");
        }

        payment.setStatus("COMPLETED");
        paymentMapper.updatePaymentStatus(payment.getId(), payment.getStatus());
        logger.info("Payment status updated to COMPLETED for payment: {}", payment);
    }

    @Transactional
    public void compensatePayment(String paymentId) {
        paymentMapper.updatePaymentStatus(paymentId, "CANCELLED");
    }
}
