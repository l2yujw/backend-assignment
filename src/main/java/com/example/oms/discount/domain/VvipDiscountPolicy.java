package com.example.oms.discount.domain;

import com.example.oms.member.domain.MemberGrade;

public class VvipDiscountPolicy implements DiscountPolicy {

    private static final int DISCOUNT_RATE = 10;

    @Override
    public boolean supports(MemberGrade grade) {
        return grade == MemberGrade.VVIP;
    }

    @Override
    public int calculateDiscount(int originalPrice) {
        return (int) Math.floor(originalPrice * (DISCOUNT_RATE / 100.0));
    }

    @Override
    public String policyName() {
        return "VVIP_RATE_10";
    }

    @Override
    public DiscountType discountType() {
        return DiscountType.RATE;
    }

    @Override
    public int discountValue() {
        return DISCOUNT_RATE;
    }
}
