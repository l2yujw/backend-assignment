package com.example.oms.payment.application;

import com.example.oms.core.annotation.Reader;
import com.example.oms.core.error.DomainException;
import com.example.oms.payment.domain.Payment;
import com.example.oms.payment.domain.error.PaymentError;
import com.example.oms.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class PaymentReader {

    private final PaymentRepository paymentRepository;

    public Payment getByIdOrThrow(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new DomainException(PaymentError.PAYMENT_NOT_FOUND, "paymentId=" + id));
    }

    public void assertNotExists(Long orderId) {
        if (paymentRepository.existsByOrderId(orderId)) {
            throw new DomainException(PaymentError.PAYMENT_ALREADY_EXISTS, "orderId=" + orderId);
        }
    }
}
