package com.example.oms.payment.application;

import com.example.oms.core.type.PaymentMethod;
import com.example.oms.member.application.MemberService;
import com.example.oms.member.application.result.MemberResult;
import com.example.oms.order.application.OrderService;
import com.example.oms.order.application.result.OrderResult;
import com.example.oms.payment.application.command.PaymentCommand;
import com.example.oms.payment.application.result.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final OrderService orderService;
    private final MemberService memberService;
    private final PaymentService paymentService;

    public PaymentResult.Paid pay(Long orderId, String paymentMethod) {
        OrderResult.OrderInfo order = orderService.getInfo(orderId);
        MemberResult.MemberInfo member = memberService.getInfo(order.memberId());
        return paymentService.pay(
                new PaymentCommand.Pay(
                        order.id(),
                        order.productName(),
                        order.originalPrice(),
                        member.id(),
                        member.grade(),
                        PaymentMethod.valueOf(paymentMethod.toUpperCase())
                )
        );
    }

    public PaymentResult.Paid getPayment(Long paymentId) {
        return paymentService.getPayment(paymentId);
    }
}
