package com.design_pattern.sidecar_pattern.transaction.mapper;

import com.design_pattern.sidecar_pattern.transaction.dto.Payment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    int insertPayment(Payment payment);

    void updatePaymentStatus(String id, String status);
}
