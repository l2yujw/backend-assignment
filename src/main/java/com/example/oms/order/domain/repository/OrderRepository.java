package com.example.oms.order.domain.repository;

import com.example.oms.order.domain.Order;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
}
