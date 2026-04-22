package com.example.oms.payment.domain;

import com.example.oms.core.annotation.AggregateRoot;
import com.example.oms.core.guard.Guard;
import com.example.oms.core.persistence.AbstractTime;
import com.example.oms.core.type.PaymentMethod;
import com.example.oms.core.type.DiscountType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@AggregateRoot
@Table(name = "payment",
        indexes = {
                @Index(name = "idx_payment_order", columnList = "order_id"),
                @Index(name = "idx_payment_member", columnList = "member_id")
        }
        )
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends AbstractTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "original_price", nullable = false)
    private int originalPrice;

    @Column(name = "discount_amount", nullable = false)
    private int discountAmount;

    @Column(name = "final_amount", nullable = false)
    private int finalAmount;

    @Column(name = "policy_name")
    private String policyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Column(name = "discount_value")
    private int discountValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "paid_at", nullable = false)
    private Instant paidAt;

    private Payment(
            Long orderId,
            Long memberId,
            String productName,
            int originalPrice,
            int discountAmount,
            int finalAmount,
            String policyName,
            DiscountType discountType,
            int discountValue,
            PaymentMethod paymentMethod,
            Instant paidAt
    ) {
        this.orderId = Guard.notNull(orderId, "orderId");
        this.memberId = Guard.notNull(memberId, "memberId");
        this.productName = Guard.notBlank(productName, "productName");
        this.originalPrice = Guard.minValue(originalPrice, 1, "originalPrice");
        this.discountAmount = discountAmount;
        this.finalAmount = Guard.minValue(finalAmount, 0, "finalAmount");
        this.policyName = policyName;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.paymentMethod = Guard.notNull(paymentMethod, "paymentMethod");
        this.paidAt = Guard.notNull(paidAt, "paidAt");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long orderId;
        private Long memberId;
        private String productName;
        private int originalPrice;
        private int discountAmount;
        private int finalAmount;
        private String policyName;
        private DiscountType discountType;
        private int discountValue;
        private PaymentMethod paymentMethod;
        private Instant paidAt;

        private Builder() {
        }

        public Builder orderId(Long v) {
            this.orderId = v;
            return this;
        }

        public Builder memberId(Long v) {
            this.memberId = v;
            return this;
        }

        public Builder productName(String v) {
            this.productName = v;
            return this;
        }

        public Builder originalPrice(int v) {
            this.originalPrice = v;
            return this;
        }

        public Builder discountAmount(int v) {
            this.discountAmount = v;
            return this;
        }

        public Builder finalAmount(int v) {
            this.finalAmount = v;
            return this;
        }

        public Builder policyName(String v) {
            this.policyName = v;
            return this;
        }

        public Builder discountType(DiscountType v) {
            this.discountType = v;
            return this;
        }

        public Builder discountValue(int v) {
            this.discountValue = v;
            return this;
        }

        public Builder paymentMethod(PaymentMethod v) {
            this.paymentMethod = v;
            return this;
        }

        public Builder paidAt(Instant v) {
            this.paidAt = v;
            return this;
        }

        public Payment build() {
            return new Payment(
                    orderId,
                    memberId,
                    productName,
                    originalPrice,
                    discountAmount,
                    finalAmount,
                    policyName,
                    discountType,
                    discountValue,
                    paymentMethod,
                    paidAt
            );
        }
    }
}
