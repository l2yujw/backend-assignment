package com.example.oms.discount.application;

import com.example.oms.core.type.PaymentMethod;
import com.example.oms.discount.application.result.DiscountResult;
import com.example.oms.discount.domain.DiscountPolicy;
import com.example.oms.core.type.MemberGrade;
import com.example.oms.discount.domain.PaymentMethodDiscountPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final List<DiscountPolicy> policies;
    private final List<PaymentMethodDiscountPolicy> methodPolicies;

    public DiscountResult.Grade applyGradeDiscount(MemberGrade grade, int originalPrice) {
        return policies.stream()
                .filter(policy -> policy.supports(grade))
                .findFirst()
                .map(
                        policy -> {
                            int discountAmount = policy.calculateDiscount(originalPrice);
                            int finalPrice = Math.max(0, originalPrice - discountAmount);

                            return new DiscountResult.Grade(
                                    policy.policyName(),
                                    policy.discountType(),
                                    policy.discountValue(),
                                    discountAmount,
                                    finalPrice
                            );
                        }
                )
                .orElse(DiscountResult.Grade.noDiscount(originalPrice));
    }

    public DiscountResult.Method applyMethodDiscount(PaymentMethod method, int priceAfterGrade) {
        return methodPolicies.stream()
                .filter(policy -> policy.supports(method))
                .findFirst()
                .map(
                        policy -> new DiscountResult.Method(
                                policy.calculateDiscount(priceAfterGrade),
                                policy.discountRate()
                        )
                )
                .orElse(DiscountResult.Method.noDiscount());
    }
}
