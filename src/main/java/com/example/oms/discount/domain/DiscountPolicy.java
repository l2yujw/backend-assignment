package com.example.oms.discount.domain;

import com.example.oms.member.domain.MemberGrade;


public interface DiscountPolicy {

    boolean supports(MemberGrade grade);

    int calculateDiscount(int originalPrice);

    String policyName();

    DiscountType discountType();

    int discountValue();
}
