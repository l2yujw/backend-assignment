package com.example.oms.payment.application;


import com.example.oms.core.type.DiscountType;
import com.example.oms.core.type.MemberGrade;
import com.example.oms.core.type.PaymentMethod;
import com.example.oms.discount.application.DiscountService;
import com.example.oms.discount.domain.*;
import com.example.oms.payment.application.command.PaymentCommand;
import com.example.oms.payment.application.result.PaymentResult;
import com.example.oms.payment.domain.DiscountHistory;
import com.example.oms.payment.domain.Payment;
import com.example.oms.payment.domain.repository.DiscountHistoryRepository;
import com.example.oms.payment.domain.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("데이터 정합성 테스트 - 정책 변경/삭제 이후 결제 및 이력 보존")
class PaymentHistoryIntegrityTest {

    private final AtomicLong paymentIdSeq = new AtomicLong(1);
    private final AtomicLong orderIdSeq = new AtomicLong(1);
    private final List<Payment> paymentStore = new ArrayList<>();
    private final List<DiscountHistory> historyStore = new ArrayList<>();

    private final PaymentRepository fakePaymentRepository = new PaymentRepository() {

        @Override
        public Payment save(Payment payment) {
            injectId(payment, paymentIdSeq.getAndIncrement());
            paymentStore.add(payment);
            return payment;
        }

        @Override
        public Optional<Payment> findById(Long id) {
            return paymentStore.stream()
                    .filter(p -> Objects.equals(p.getId(), id))
                    .findFirst();
        }

        @Override
        public boolean existsByOrderId(Long orderId) {
            return paymentStore.stream()
                    .anyMatch(p -> Objects.equals(p.getOrderId(), orderId));
        }

        private void injectId(Payment payment, long id) {
            try {
                var field = Payment.class.getDeclaredField("id");
                field.setAccessible(true);
                field.set(payment, id);
            } catch (Exception e) {
                throw new IllegalStateException("테스트용 Payment ID 주입 실패", e);
            }
        }
    };

    private final DiscountHistoryRepository fakeHistoryRepository = new DiscountHistoryRepository() {
        @Override
        public DiscountHistory save(DiscountHistory history) {
            historyStore.add(history);
            return history;
        }

        @Override
        public List<DiscountHistory> findByPaymentId(Long paymentId) {
            return historyStore.stream()
                    .filter(history -> Objects.equals(history.getPaymentId(), paymentId))
                    .toList();
        }
    };

    @BeforeEach
    void resetStores() {
        paymentStore.clear();
        historyStore.clear();
        paymentIdSeq.set(1L);
        orderIdSeq.set(1L);
    }

    private PaymentService buildService(List<DiscountPolicy> gradePolicies) {
        return new PaymentService(
                new PaymentWriter(fakePaymentRepository),
                new PaymentReader(fakePaymentRepository),
                new DiscountHistoryWriter(fakeHistoryRepository),
                new DiscountHistoryReader(fakeHistoryRepository),
                new DiscountService(gradePolicies, List.of(new PointPaymentDiscountPolicy()))
        );
    }

    private PaymentCommand.Pay creditCardPay(MemberGrade grade, int price) {
        return new PaymentCommand.Pay(
                orderIdSeq.getAndIncrement(),
                "테스트 상품",
                price,
                100L,
                grade,
                PaymentMethod.CREDIT_CARD
        );
    }

    private static final List<DiscountPolicy> DEFAULT_POLICIES =
            List.of(new NormalDiscountPolicy(), new VipDiscountPolicy(), new VvipDiscountPolicy());

    @Nested
    @DisplayName("정책 변경 이후")
    class AfterPolicyChange {

        @Test
        @DisplayName("변경 이전 결제의 이력은 당시 정책명과 할인 금액을 그대로 유지한다")
        void history_preserves_snapshot_of_policy_at_payment_time() {

            // given: 기존 VIP 정책(1000원)으로 결제
            PaymentService oldService = buildService(DEFAULT_POLICIES);
            PaymentResult.Paid firstPayment = oldService.pay(creditCardPay(MemberGrade.VIP, 10_000));

            // when: VIP 할인 금액을 2000원으로 올린 새 정책으로 교체 후 추가 결제
            DiscountPolicy updatedVip = fixedDiscount(MemberGrade.VIP, "VIP_FIX_2000", 2_000);
            PaymentService newService = buildService(
                    List.of(new NormalDiscountPolicy(), updatedVip, new VvipDiscountPolicy())
            );
            newService.pay(creditCardPay(MemberGrade.VIP, 10_000));

            // then: 첫 번째 결제의 이력은 변경 전 정책 그대로
            List<DiscountHistory> histories = fakeHistoryRepository.findByPaymentId(firstPayment.paymentId());

            assertThat(histories).hasSize(1);
            assertThat(histories.getFirst().getPolicyName()).isEqualTo("VIP_FIX_1000");
            assertThat(histories.getFirst().getGradeDiscountAmount()).isEqualTo(1_000);
        }

        @Test
        @DisplayName("변경 이후 결제의 이력은 새 정책을 반영한다")
        void history_reflects_new_policy_after_change() {

            // given: 기존 서비스로 먼저 결제
            buildService(DEFAULT_POLICIES).pay(creditCardPay(MemberGrade.VIP, 10_000));

            // when: 새 정책으로 결제
            DiscountPolicy updatedVip = fixedDiscount(MemberGrade.VIP, "VIP_FIX_2000", 2_000);
            PaymentService newService = buildService(
                    List.of(new NormalDiscountPolicy(), updatedVip, new VvipDiscountPolicy())
            );
            PaymentResult.Paid secondPayment = newService.pay(creditCardPay(MemberGrade.VIP, 10_000));

            // then: 두 번째 결제 이력은 새 정책을 반영
            List<DiscountHistory> histories = fakeHistoryRepository.findByPaymentId(secondPayment.paymentId());

            assertThat(histories).hasSize(1);
            assertThat(histories.getFirst().getPolicyName()).isEqualTo("VIP_FIX_2000");
            assertThat(histories.getFirst().getGradeDiscountAmount()).isEqualTo(2_000);
        }
    }

    @Nested
    @DisplayName("정책 삭제 이후")
    class AfterPolicyDeletion {

        @Test
        @DisplayName("삭제 이전 결제의 이력은 당시 정책명과 할인 금액을 그대로 유지한다")
        void history_preserved_after_policy_deletion() {

            // given: VVIP 정책이 있는 서비스로 결제 (20000원 10% = 2000원 할인)
            PaymentService serviceWithVvip = buildService(DEFAULT_POLICIES);
            PaymentResult.Paid vvipPayment = serviceWithVvip.pay(creditCardPay(MemberGrade.VVIP, 20_000));

            // when: VVIP 정책을 제거한 서비스로 추가 결제
            PaymentService serviceWithoutVvip = buildService(
                    List.of(new NormalDiscountPolicy(), new VipDiscountPolicy())
            );
            serviceWithoutVvip.pay(creditCardPay(MemberGrade.VVIP, 20_000));

            // then: 삭제 이전 결제의 이력은 영향 없음
            List<DiscountHistory> histories = fakeHistoryRepository.findByPaymentId(vvipPayment.paymentId());

            assertThat(histories).hasSize(1);
            assertThat(histories.getFirst().getPolicyName()).isEqualTo("VVIP_RATE_10");
            assertThat(histories.getFirst().getGradeDiscountAmount()).isEqualTo(2_000);
        }

        @Test
        @DisplayName("정책 삭제 후 해당 등급으로 결제하면 할인 없이 원가로 청구된다")
        void no_discount_applied_when_policy_is_deleted() {

            // given: VVIP 정책이 없는 서비스
            PaymentService serviceWithoutVvip = buildService(
                    List.of(new NormalDiscountPolicy(), new VipDiscountPolicy())
            );

            // when
            PaymentResult.Paid paid = serviceWithoutVvip.pay(creditCardPay(MemberGrade.VVIP, 20_000));

            // then: 할인 없이 원가 그대로
            assertThat(paid.finalAmount()).isEqualTo(20_000);
            assertThat(paid.discountAmount()).isEqualTo(0);
        }

        @Test
        @DisplayName("한 정책의 삭제가 다른 등급의 이력에 영향을 주지 않는다")
        void policies_are_independent_deletion_does_not_affect_other_histories() {

            // given: VIP와 VVIP 각각 결제
            PaymentService fullService = buildService(DEFAULT_POLICIES);
            PaymentResult.Paid vipPayment = fullService.pay(creditCardPay(MemberGrade.VIP, 10_000));
            PaymentResult.Paid vvipPayment = fullService.pay(creditCardPay(MemberGrade.VVIP, 20_000));

            // when: VVIP 정책만 제거
            buildService(List.of(new NormalDiscountPolicy(), new VipDiscountPolicy()))
                    .pay(creditCardPay(MemberGrade.VVIP, 20_000));

            // then: VIP 이력도, VVIP 이력도 각자 보존됨
            assertThat(fakeHistoryRepository.findByPaymentId(vipPayment.paymentId()))
                    .hasSize(1)
                    .first()
                    .satisfies(history -> {
                        assertThat(history.getPolicyName()).isEqualTo("VIP_FIX_1000");
                        assertThat(history.getGradeDiscountAmount()).isEqualTo(1_000);
                    });

            assertThat(fakeHistoryRepository.findByPaymentId(vvipPayment.paymentId()))
                    .hasSize(1)
                    .first()
                    .satisfies(history -> {
                        assertThat(history.getPolicyName()).isEqualTo("VVIP_RATE_10");
                        assertThat(history.getGradeDiscountAmount()).isEqualTo(2_000);
                    });
        }
    }

    private static DiscountPolicy fixedDiscount(MemberGrade targetGrade, String name, int amount) {
        return new DiscountPolicy() {
            public boolean supports(MemberGrade memberGrade) {
                return memberGrade == targetGrade;
            }

            public int calculateDiscount(int p) {
                return Math.min(amount, p);
            }

            public String policyName() {
                return name;
            }

            public DiscountType discountType() {
                return DiscountType.FIXED;
            }

            public int discountValue() {
                return amount;
            }
        };
    }
}
