package com.example.oms.discount.domain;

import com.example.oms.member.domain.MemberGrade;

public class VipDiscountPolicy implements DiscountPolicy {

    private static final int DISCOUNT_AMOUNT = 1_000;

    @Override
    public boolean supports(MemberGrade grade) {
        return grade == MemberGrade.VIP;
    }

    @Override
    public int calculateDiscount(int originalPrice) {
        return Math.min(DISCOUNT_AMOUNT, originalPrice);
    }

    @Override
    public String policyName() {
        return "VIP_FIX_1000";
    }

    @Override
    public DiscountType discountType() {
        return DiscountType.FIXED;
    }

    @Override
    public int discountValue() {
        return DISCOUNT_AMOUNT;
    }
}
