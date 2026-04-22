package com.example.oms.discount.application;

import com.example.oms.discount.application.result.DiscountResult;
import com.example.oms.discount.domain.DiscountPolicy;
import com.example.oms.member.domain.MemberGrade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    private final List<DiscountPolicy> policies;

    public DiscountService(List<DiscountPolicy> policies) {
        this.policies = policies;
    }

    public DiscountResult applyGradeDiscount(MemberGrade grade, int originalPrice) {
        return policies.stream()
                .filter(policy -> policy.supports(grade))
                .findFirst()
                .map(policy -> {
                    int discountAmount = policy.calculateDiscount(originalPrice);
                    int finalPrice = Math.max(0, originalPrice - discountAmount);

                    return new DiscountResult(
                            policy.policyName(),
                            policy.discountType(),
                            policy.discountValue(),
                            discountAmount,
                            finalPrice
                    );
                })
                .orElse(DiscountResult.noDiscount(originalPrice));
    }
}
