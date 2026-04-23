package com.example.oms.payment.api.response;

import com.example.oms.payment.application.result.PaymentResult;

import java.time.Instant;
import java.util.List;

public final class DiscountHistoryResponse {

    private DiscountHistoryResponse() {
    }

    public record Detail(
            Long id,
            Long paymentId,
            String memberGrade,
            String policyName,
            String discountType,
            int discountValue,
            int gradeDiscountAmount,
            int paymentMethodDiscountRate,
            int paymentMethodDiscountAmount,
            int originalPrice,
            int finalAmount,
            Instant createdAt
    ) {
        public static Detail from(PaymentResult.DiscountHistoryInfo info) {
            return new Detail(
                    info.id(),
                    info.paymentId(),
                    info.memberGrade().name(),
                    info.policyName(),
                    info.discountType().name(),
                    info.discountValue(),
                    info.gradeDiscountAmount(),
                    info.paymentMethodDiscountRate(),
                    info.paymentMethodDiscountAmount(),
                    info.originalPrice(),
                    info.finalAmount(),
                    info.createdAt()
            );
        }

        public static List<Detail> fromList(List<PaymentResult.DiscountHistoryInfo> infos) {
            return infos.stream().map(Detail::from).toList();
        }
    }
}
