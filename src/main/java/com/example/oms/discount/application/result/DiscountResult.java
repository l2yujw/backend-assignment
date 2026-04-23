package com.example.oms.discount.application.result;

import com.example.oms.core.type.DiscountType;

public final class DiscountResult {

    private DiscountResult() {
    }

    public record Grade(
            String policyName,
            DiscountType discountType,
            int discountValue,
            int discountAmount,
            int finalPrice
    ) {
        public static DiscountResult.Grade noDiscount(int originalPrice) {
            return new DiscountResult.Grade(
                    "NORMAL_NO_DISCOUNT",
                    DiscountType.FIXED,
                    0,
                    0,
                    originalPrice
            );
        }
    }

    public record Method(
            int methodDiscountAmount,
            int methodDiscountRate
    ) {
        public static DiscountResult.Method noDiscount() {
            return new DiscountResult.Method(0,0);
        }
    }
}
