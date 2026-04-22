package com.example.oms.discount.domain;

import com.example.oms.core.type.DiscountType;
import com.example.oms.core.type.MemberGrade;


public interface DiscountPolicy {

    boolean supports(MemberGrade grade);

    int calculateDiscount(int originalPrice);

    String policyName();

    DiscountType discountType();

    int discountValue();
}
