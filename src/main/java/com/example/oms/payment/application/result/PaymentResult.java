package com.example.oms.payment.application.result;

import com.example.oms.core.type.DiscountType;
import com.example.oms.core.type.MemberGrade;
import com.example.oms.core.type.PaymentMethod;
import com.example.oms.payment.domain.DiscountHistory;
import com.example.oms.payment.domain.Payment;

import java.time.Instant;
import java.util.List;

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
        public static Paid from(Payment payment) {
            return new Paid(
                    payment.getId(),
                    payment.getOrderId(),
                    payment.getProductName(),
                    payment.getOriginalPrice(),
                    payment.getDiscountAmount(),
                    payment.getFinalAmount(),
                    payment.getPolicyName(),
                    payment.getPaymentMethod().name(),
                    payment.getPaidAt()
            );
        }
    }

    public record DiscountHistoryInfo(
            Long id,
            Long paymentId,
            MemberGrade memberGrade,
            String policyName,
            DiscountType discountType,
            int discountValue,
            int gradeDiscountAmount,
            PaymentMethod paymentMethod,
            int paymentMethodDiscountRate,
            int paymentMethodDiscountAmount,
            int originalPrice,
            int finalAmount,
            Instant createdAt
    ) {
        public static DiscountHistoryInfo from(DiscountHistory history) {
            return new DiscountHistoryInfo(
                    history.getId(),
                    history.getPaymentId(),
                    history.getMemberGrade(),
                    history.getPolicyName(),
                    history.getDiscountType(),
                    history.getDiscountValue(),
                    history.getGradeDiscountAmount(),
                    history.getPaymentMethod(),
                    history.getPaymentMethodDiscountRate(),
                    history.getPaymentMethodDiscountAmount(),
                    history.getOriginalPrice(),
                    history.getFinalAmount(),
                    history.getCreatedAt()
            );
        }

        public static List<DiscountHistoryInfo> fromList(List<DiscountHistory> list) {
            return list.stream().map(DiscountHistoryInfo::from).toList();
        }
    }
}
