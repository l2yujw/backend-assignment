package com.example.oms.payment.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public final class PaymentRequest {

    private PaymentRequest() {}

    public record Pay(
            @NotNull Long orderId,
            @NotBlank String paymentMethod
    ) {}
}
