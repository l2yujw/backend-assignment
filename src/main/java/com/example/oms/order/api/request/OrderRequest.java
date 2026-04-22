package com.example.oms.order.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class OrderRequest {

    private OrderRequest() {
    }

    public record Place(
            @NotNull Long memberId,
            @NotBlank String productName,
            @Min(1) int originalPrice
    ) {
    }
}
