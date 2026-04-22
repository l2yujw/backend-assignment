package com.example.oms.discount.domain;

import com.example.oms.member.domain.MemberGrade;

public class NormalDiscountPolicy implements DiscountPolicy {

    @Override
    public boolean supports(MemberGrade grade) {
        return grade == MemberGrade.NORMAL;
    }

    @Override
    public int calculateDiscount(int originalPrice) {
        return 0;
    }

    @Override
    public String policyName() {
        return "NORMAL_NO_DISCOUNT";
    }

    @Override
    public DiscountType discountType() {
        return DiscountType.FIXED;
    }

    @Override
    public int discountValue() {
        return 0;
    }
}
