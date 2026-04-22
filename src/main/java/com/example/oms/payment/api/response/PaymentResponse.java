package com.example.oms.payment.api.response;

import com.example.oms.payment.application.result.PaymentResult;

import java.time.Instant;

public final class PaymentResponse {

    private PaymentResponse() {
    }

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
        public static Paid from(PaymentResult.Paid result) {
            return new Paid(
                    result.paymentId(),
                    result.orderId(),
                    result.productName(),
                    result.originalPrice(),
                    result.discountAmount(),
                    result.finalAmount(),
                    result.policyName(),
                    result.paymentMethod(),
                    result.paidAt()
            );
        }
    }
}
