package com.example.oms.order.application;

import com.example.oms.order.application.command.OrderCommand;
import com.example.oms.order.application.result.OrderResult;
import com.example.oms.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderReader orderReader;
    private final OrderWriter orderWriter;

    @Transactional
    public OrderResult.Placed place(OrderCommand.Place command) {
        Order order = Order.place(
                command.memberId(),
                command.productName(),
                command.originalPrice()
        );
        orderWriter.create(order);

        return new OrderResult.Placed(order.getId());
    }

    @Transactional(readOnly = true)
    public OrderResult.OrderInfo getInfo(Long orderId) {
        Order order = orderReader.getByIdOrThrow(orderId);

        return OrderResult.OrderInfo.from(order);
    }
}
