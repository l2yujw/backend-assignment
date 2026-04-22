package com.example.oms.order.api;

import com.example.oms.core.response.ApiResponse;
import com.example.oms.order.api.request.OrderRequest;
import com.example.oms.order.api.response.OrderResponse;
import com.example.oms.order.application.OrderFacade;
import com.example.oms.order.application.command.OrderCommand;
import com.example.oms.platform.web.response.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse.Placed>> place(
            @Valid @RequestBody OrderRequest.Place request
    ) {
        OrderResponse.Placed result = OrderResponse.Placed.from(
                orderFacade.place(
                        new OrderCommand.Place(
                                request.memberId(),
                                request.productName(),
                                request.originalPrice()
                        )
                )
        );

        return ApiResponses.created(URI.create("/api/v1/orders/" + result.orderId()), result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse.OrderInfo>> getInfo(
            @PathVariable Long orderId
    ) {
        return ApiResponses.ok(OrderResponse.OrderInfo.from(orderFacade.getInfo(orderId)));
    }
}
