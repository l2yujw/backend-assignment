package com.example.oms.order.infra;

import com.example.oms.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long> {}
