package com.example.oms.payment.application.result;

import com.example.oms.payment.domain.Payment;

import java.time.Instant;

public final class PaymentResult {

    private PaymentResult() {}

    public record Paid(
            Long paymentId,
            Long orderId,
            String productName,
            int originalPrice,
            int discountAmount,
            int finalAmount,
            String policyName,
            String paymentMethod,
            Instant paidAt
    ) {
        public static Paid from(Payment p) {
            return new Paid(
                    p.getId(),
                    p.getOrderId(),
                    p.getProductName(),
                    p.getOriginalPrice(),
                    p.getDiscountAmount(),
                    p.getFinalAmount(),
                    p.getPolicyName(),
                    p.getPaymentMethod().name(),
                    p.getPaidAt()
            );
        }
    }
}
