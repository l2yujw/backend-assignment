package com.example.oms.payment.application;

import com.example.oms.discount.application.DiscountService;
import com.example.oms.discount.application.result.DiscountResult;
import com.example.oms.payment.application.command.PaymentCommand;
import com.example.oms.payment.application.result.PaymentResult;
import com.example.oms.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentWriter paymentWriter;
    private final PaymentReader paymentReader;
    private final DiscountService discountService;

    @Transactional
    public PaymentResult.Paid pay(PaymentCommand.Pay command) {
        paymentReader.assertNotExists(command.orderId());

        DiscountResult gradeDiscount = discountService.applyGradeDiscount(
                command.memberGrade(), command.originalPrice()
        );

        int finalAmount = gradeDiscount.finalPrice();

        Payment payment = Payment.builder()
                .orderId(command.orderId())
                .memberId(command.memberId())
                .productName(command.productName())
                .originalPrice(command.originalPrice())
                .discountAmount(gradeDiscount.discountAmount())
                .finalAmount(finalAmount)
                .policyName(gradeDiscount.policyName())
                .discountType(gradeDiscount.discountType())
                .discountValue(gradeDiscount.discountValue())
                .paymentMethod(command.paymentMethod())
                .paidAt(Instant.now())
                .build();

        paymentWriter.save(payment);

        return PaymentResult.Paid.from(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResult.Paid getPayment(Long paymentId) {
        return PaymentResult.Paid.from(paymentReader.getByIdOrThrow(paymentId));
    }
}
