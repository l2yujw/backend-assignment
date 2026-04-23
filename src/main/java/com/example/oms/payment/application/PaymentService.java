package com.example.oms.payment.application;

import com.example.oms.discount.application.DiscountService;
import com.example.oms.discount.application.result.DiscountResult;
import com.example.oms.payment.application.command.PaymentCommand;
import com.example.oms.payment.application.result.PaymentResult;
import com.example.oms.payment.domain.DiscountHistory;
import com.example.oms.payment.domain.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentWriter paymentWriter;
    private final PaymentReader paymentReader;
    private final DiscountHistoryWriter discountHistoryWriter;
    private final DiscountHistoryReader discountHistoryReader;
    private final DiscountService discountService;

    @Transactional
    public PaymentResult.Paid pay(PaymentCommand.Pay command) {
        paymentReader.assertNotExists(command.orderId());

        DiscountResult.Grade gradeDiscount = discountService.applyGradeDiscount(
                command.memberGrade(),
                command.originalPrice()
        );

        DiscountResult.Method methodDiscount = discountService.applyMethodDiscount(
                command.paymentMethod(),
                gradeDiscount.finalPrice()
        );

        int finalAmount = Math.max(0, gradeDiscount.finalPrice() - methodDiscount.methodDiscountAmount());

        Payment payment = Payment.builder()
                .orderId(command.orderId())
                .memberId(command.memberId())
                .productName(command.productName())
                .originalPrice(command.originalPrice())
                .discountAmount(gradeDiscount.discountAmount() + methodDiscount.methodDiscountAmount())
                .finalAmount(finalAmount)
                .policyName(gradeDiscount.policyName())
                .discountType(gradeDiscount.discountType())
                .discountValue(gradeDiscount.discountValue())
                .paymentMethod(command.paymentMethod())
                .paidAt(Instant.now())
                .build();
        paymentWriter.save(payment);

        DiscountHistory history = DiscountHistory.record(
                payment.getId(),
                command.memberGrade(),
                gradeDiscount.policyName(),
                gradeDiscount.discountType(),
                gradeDiscount.discountValue(),
                gradeDiscount.discountAmount(),
                command.paymentMethod(),
                methodDiscount.methodDiscountRate(),
                methodDiscount.methodDiscountAmount(),
                command.originalPrice(),
                finalAmount
        );
        discountHistoryWriter.save(history);

        return PaymentResult.Paid.from(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResult.Paid getPayment(Long paymentId) {
        return PaymentResult.Paid.from(paymentReader.getByIdOrThrow(paymentId));
    }

    @Transactional(readOnly = true)
    public List<PaymentResult.DiscountHistoryInfo> getDiscountHistory(Long paymentId) {
        return PaymentResult.DiscountHistoryInfo.fromList(
                discountHistoryReader.getByPaymentId(paymentId)
        );
    }
}
