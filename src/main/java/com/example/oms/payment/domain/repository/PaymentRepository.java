package com.example.oms.payment.domain.repository;

import com.example.oms.payment.domain.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    boolean existsByOrderId(Long id);
}
