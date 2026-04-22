package com.example.oms.payment.domain;

import com.example.oms.core.persistence.AbstractTime;
import com.example.oms.core.type.DiscountType;
import com.example.oms.core.type.MemberGrade;
import com.example.oms.core.type.PaymentMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(
        name = "discount_history",
        indexes = @Index(name = "idx_discount_history_payment", columnList = "payment_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountHistory extends AbstractTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_grade", nullable = false)
    private MemberGrade memberGrade;

    @Column(name = "policy_name", nullable = false)
    private String policyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false)
    private int discountValue;

    @Column(name = "grade_discount_amount", nullable = false)
    private int gradeDiscountAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_method_discount_rate", nullable = false)
    private int paymentMethodDiscountRate;

    @Column(name = "payment_method_discount_amount", nullable = false)
    private int paymentMethodDiscountAmount;

    @Column(name = "original_price", nullable = false)
    private int originalPrice;

    @Column(name = "final_amount", nullable = false)
    private int finalAmount;

    private DiscountHistory(
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
            int finalAmount
    ) {
        this.paymentId = paymentId;
        this.memberGrade = memberGrade;
        this.policyName = policyName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.gradeDiscountAmount = gradeDiscountAmount;
        this.paymentMethod = paymentMethod;
        this.paymentMethodDiscountRate = paymentMethodDiscountRate;
        this.paymentMethodDiscountAmount = paymentMethodDiscountAmount;
        this.originalPrice = originalPrice;
        this.finalAmount = finalAmount;
    }

    public static DiscountHistory record(
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
            int finalAmount
    ) {
        return new DiscountHistory(
                paymentId,
                memberGrade,
                policyName,
                discountType,
                discountValue,
                gradeDiscountAmount,
                paymentMethod,
                paymentMethodDiscountRate,
                paymentMethodDiscountAmount,
                originalPrice,
                finalAmount
        );
    }
}
