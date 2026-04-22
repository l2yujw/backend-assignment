package com.example.oms.order.infra;

import com.example.oms.order.domain.Order;
import com.example.oms.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpa;

    @Override
    public Order save(Order order) {
        return jpa.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpa.findById(id);
    }
}
