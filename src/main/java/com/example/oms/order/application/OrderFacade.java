package com.example.oms.order.application;

import com.example.oms.member.application.MemberService;
import com.example.oms.order.application.command.OrderCommand;
import com.example.oms.order.application.result.OrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final MemberService memberService;
    private final OrderService  orderService;

    public OrderResult.Placed place(OrderCommand.Place command) {
        memberService.assertExists(command.memberId());
        return orderService.place(command);
    }

    public OrderResult.OrderInfo getInfo(Long orderId) {
        return orderService.getInfo(orderId);
    }
}
