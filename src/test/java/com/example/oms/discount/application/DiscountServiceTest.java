package com.example.oms.discount.application;

import com.example.oms.core.type.PaymentMethod;
import com.example.oms.discount.application.result.DiscountResult;
import com.example.oms.discount.domain.NormalDiscountPolicy;
import com.example.oms.discount.domain.PointPaymentDiscountPolicy;
import com.example.oms.discount.domain.VipDiscountPolicy;
import com.example.oms.discount.domain.VvipDiscountPolicy;
import com.example.oms.core.type.MemberGrade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("등급별 할인 정책 테스트")
class DiscountServiceTest {

    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService(
                List.of(
                    new NormalDiscountPolicy(),
                    new VipDiscountPolicy(),
                    new VvipDiscountPolicy()
                ),
                List.of(
                        new PointPaymentDiscountPolicy()
                )
        );
    }

    @Nested
    @DisplayName("NORMAL 등급")
    class NormalGrade {

        @Test
        @DisplayName("할인이 적용되지 않아 원가가 그대로 최종 금액이 된다")
        void noDiscount() {
            DiscountResult.Grade result = discountService.applyGradeDiscount(MemberGrade.NORMAL, 10_000);

            assertThat(result.discountAmount()).isEqualTo(0);
            assertThat(result.finalPrice()).isEqualTo(10_000);
        }
    }

    @Nested
    @DisplayName("VIP 등급 - 1000원 고정 할인")
    class VipGrade {

        @Test
        @DisplayName("10000원 주문 시 1000원 할인 -> 최종 9000원")
        void fix1000Discount() {
            DiscountResult.Grade result = discountService.applyGradeDiscount(MemberGrade.VIP, 10_000);

            assertThat(result.discountAmount()).isEqualTo(1_000);
            assertThat(result.finalPrice()).isEqualTo(9_000);
            assertThat(result.policyName()).isEqualTo("VIP_FIX_1000");
        }

        @Test
        @DisplayName("원가가 할인 금액보다 작으면 최종 금액은 0원")
        void priceUnderDiscount() {
            DiscountResult.Grade result = discountService.applyGradeDiscount(MemberGrade.VIP, 500);

            assertThat(result.finalPrice()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("VVIP 등급 - 10% 비율 할인")
    class VVipGrade {

        @Test
        @DisplayName("10000원 주문 시 10% = 1000원 할인 -> 최종 9000원")
        void rate10Discount() {
            DiscountResult.Grade result = discountService.applyGradeDiscount(MemberGrade.VVIP, 10_000);

            assertThat(result.discountAmount()).isEqualTo(1_000);
            assertThat(result.finalPrice()).isEqualTo(9_000);
            assertThat(result.policyName()).isEqualTo("VVIP_RATE_10");
        }
    }

    @Nested
    @DisplayName("등급 할인 + 결제수단 할인 중복 적용 테스트")
    class CombinedDiscount {

        @Test
        @DisplayName("VIP + POINT -> 등급 할인 후 포인트 5% 할인 추가 적용")
        void vip_point_discount() {
            int originalPrice = 10_000;

            DiscountResult.Grade grade =
                    discountService.applyGradeDiscount(MemberGrade.VIP, originalPrice);

            DiscountResult.Method method =
                    discountService.applyMethodDiscount(
                            PaymentMethod.POINT,
                            grade.finalPrice()
                    );

            int finalPrice = grade.finalPrice() - method.methodDiscountAmount();

            assertThat(finalPrice).isEqualTo(8_550);
        }

        @Test
        @DisplayName("VVIP + POINT -> 등급 할인 후 포인트 5% 할인 추가 적용")
        void vvip_point_discount() {
            int originalPrice = 10_000;

            DiscountResult.Grade grade =
                    discountService.applyGradeDiscount(MemberGrade.VVIP, originalPrice);

            DiscountResult.Method method =
                    discountService.applyMethodDiscount(
                            PaymentMethod.POINT,
                            grade.finalPrice()
                    );

            int finalPrice = grade.finalPrice() - method.methodDiscountAmount();

            assertThat(finalPrice).isEqualTo(8_550);
        }

        @Test
        @DisplayName("NORMAL + CREDIT_CARD -> 할인 없음")
        void normal_credit_no_discount() {
            int originalPrice = 10_000;

            DiscountResult.Grade grade =
                    discountService.applyGradeDiscount(MemberGrade.NORMAL, originalPrice);

            DiscountResult.Method method =
                    discountService.applyMethodDiscount(
                            PaymentMethod.CREDIT_CARD,
                            grade.finalPrice()
                    );

            int finalPrice = grade.finalPrice() - method.methodDiscountAmount();

            assertThat(finalPrice).isEqualTo(10_000);
            assertThat(method.methodDiscountAmount()).isEqualTo(0);
        }

        @Test
        @DisplayName("NORMAL + POINT -> 결제수단 할인만 적용")
        void normal_point_only_method_discount() {
            int originalPrice = 10_000;

            DiscountResult.Grade grade =
                    discountService.applyGradeDiscount(MemberGrade.NORMAL, originalPrice);

            DiscountResult.Method method =
                    discountService.applyMethodDiscount(
                            PaymentMethod.POINT,
                            grade.finalPrice()
                    );

            int finalPrice = grade.finalPrice() - method.methodDiscountAmount();

            assertThat(finalPrice).isEqualTo(9_500);
        }
    }
}
