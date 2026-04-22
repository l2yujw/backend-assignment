package com.example.oms.payment.infra;

import com.example.oms.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByOrderId(Long orderId);
}
