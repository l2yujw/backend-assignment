package com.example.oms.order.application;

import com.example.oms.core.annotation.Writer;
import com.example.oms.order.domain.Order;
import com.example.oms.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderRepository orderRepository;

    public Order create(Order order) {
        return orderRepository.save(order);
    }
}
