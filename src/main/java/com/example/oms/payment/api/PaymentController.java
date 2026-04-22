package com.example.oms.payment.api;

import com.example.oms.core.response.ApiResponse;
import com.example.oms.payment.api.request.PaymentRequest;
import com.example.oms.payment.api.response.DiscountHistoryResponse;
import com.example.oms.payment.api.response.PaymentResponse;
import com.example.oms.payment.application.PaymentFacade;
import com.example.oms.payment.application.PaymentService;
import com.example.oms.platform.web.response.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacade paymentFacade;
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse.Paid>> pay(
            @Valid @RequestBody PaymentRequest.Pay request
    ) {
        PaymentResponse.Paid result = PaymentResponse.Paid.from(
                paymentFacade.pay(request.orderId(), request.paymentMethod())
        );

        return ApiResponses.created(URI.create("/api/v1/payments/" + result.paymentId()), result);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse.Paid>> getPayment(
            @PathVariable Long paymentId
    ) {
        return ApiResponses.ok(PaymentResponse.Paid.from(paymentService.getPayment(paymentId)));
    }

    @GetMapping("/{paymentId}/discount-history")
    public ResponseEntity<ApiResponse<List<DiscountHistoryResponse.Detail>>> getDiscountHistory(
            @PathVariable Long paymentId
    ) {
        return ApiResponses.ok(
                DiscountHistoryResponse.Detail.fromList(paymentService.getDiscountHistory(paymentId))
        );
    }
}
