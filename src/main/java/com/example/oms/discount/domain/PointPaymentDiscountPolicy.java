package com.example.oms.discount.domain;

import com.example.oms.core.type.PaymentMethod;

public class PointPaymentDiscountPolicy implements PaymentMethodDiscountPolicy {

    private static final int DISCOUNT_RATE = 5;

    @Override
    public boolean supports(PaymentMethod method) {
        return method == PaymentMethod.POINT;
    }

    @Override
    public int discountRate() {
        return DISCOUNT_RATE;
    }

    @Override
    public int calculateDiscount(int priceAfterGradeDiscount) {
        return (int) Math.floor(priceAfterGradeDiscount * (DISCOUNT_RATE / 100.0));
    }
}
