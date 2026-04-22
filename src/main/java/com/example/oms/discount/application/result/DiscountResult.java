package com.example.oms.discount.application.result;

import com.example.oms.discount.domain.DiscountType;

public record DiscountResult(
        String policyName,
        DiscountType discountType,
        int discountValue,
        int discountAmount,
        int finalPrice
) {
    public static DiscountResult noDiscount(int originalPrice) {
        return new DiscountResult(
                "NORMAL_NO_DISCOUNT",
                DiscountType.FIXED,
                0,
                0,
                originalPrice
        );
    }
}
