package com.example.oms.order.application;

import com.example.oms.core.annotation.Reader;
import com.example.oms.core.error.DomainException;
import com.example.oms.order.domain.Order;
import com.example.oms.order.domain.error.OrderError;
import com.example.oms.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class OrderReader {

    private final OrderRepository orderRepository;

    public Order getByIdOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DomainException(OrderError.ORDER_NOT_FOUND, "orderId=" + id));
    }
}
