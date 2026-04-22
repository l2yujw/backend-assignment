package com.example.oms.order.api.response;

import com.example.oms.order.application.result.OrderResult;

public final class OrderResponse {

    private OrderResponse() {
    }

    public record Placed(Long orderId) {
        public static Placed from(OrderResult.Placed result) {
            return new Placed(result.orderId());
        }
    }

    public record OrderInfo(
            Long id,
            Long memberId,
            String productName,
            int originalPrice
    ) {
        public static OrderInfo from(OrderResult.OrderInfo result) {
            return new OrderInfo(
                    result.id(),
                    result.memberId(),
                    result.productName(),
                    result.originalPrice()
            );
        }
    }
}
