package com.example.oms.discount.domain;


import com.example.oms.core.type.PaymentMethod;

public interface PaymentMethodDiscountPolicy {

    boolean supports(PaymentMethod method);

    int discountRate();

    int calculateDiscount(int priceAfterGradeDiscount);
}
