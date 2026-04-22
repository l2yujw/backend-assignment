package com.example.oms.payment.application;

import com.example.oms.core.annotation.Writer;
import com.example.oms.payment.domain.Payment;
import com.example.oms.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class PaymentWriter {

    private final PaymentRepository paymentRepository;

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }
}
