package com.example.oms.payment.application.command;

import com.example.oms.core.type.MemberGrade;
import com.example.oms.core.type.PaymentMethod;

public final class PaymentCommand {

    private PaymentCommand() {}

    public record Pay(
            Long orderId,
            String productName,
            int originalPrice,
            Long memberId,
            MemberGrade memberGrade,
            PaymentMethod paymentMethod
    ) {
    }
}
