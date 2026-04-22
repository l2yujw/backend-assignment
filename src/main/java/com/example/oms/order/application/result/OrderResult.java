package com.example.oms.order.application.result;

import com.example.oms.order.domain.Order;

public final class OrderResult {

    private OrderResult() {}

    public record Placed(Long orderId) {}

    public record OrderInfo(
            Long id,
            Long memberId,
            String productName,
            int originalPrice
    ) {
        public static OrderInfo from(Order order) {
            return new OrderInfo(
                    order.getId(),
                    order.getMemberId(),
                    order.getProductName(),
                    order.getOriginalPrice()
            );
        }
    }
}
