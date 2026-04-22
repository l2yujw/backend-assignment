package com.example.oms.payment.infra;

import com.example.oms.payment.domain.Payment;
import com.example.oms.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final JpaPaymentRepository jpa;

    @Override
    public Payment save(Payment p) {
        return jpa.save(p);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return jpa.existsByOrderId(orderId);
    }
}
