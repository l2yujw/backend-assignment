package com.example.oms.discount.application;

import com.example.oms.discount.application.result.DiscountResult;
import com.example.oms.discount.domain.NormalDiscountPolicy;
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
                )
        );
    }

    @Nested
    @DisplayName("NORMAL 등급")
    class NormalGrade {

        @Test
        @DisplayName("할인이 적용되지 않아 원가가 그대로 최종 금액이 된다")
        void noDiscount() {
            DiscountResult result = discountService.applyGradeDiscount(MemberGrade.NORMAL, 10_000);

            assertThat(result.discountAmount()).isEqualTo(0);
            assertThat(result.finalPrice()).isEqualTo(10_000);
        }
    }

    @Nested
    @DisplayName("VIP 등급 - 1000원 고정 할인")
    class VipGrade {

        @Test
        @DisplayName("10000원 주문 시 1000원 할인 → 최종 9000원")
        void fix1000Discount() {
            DiscountResult result = discountService.applyGradeDiscount(MemberGrade.VIP, 10_000);

            assertThat(result.discountAmount()).isEqualTo(1_000);
            assertThat(result.finalPrice()).isEqualTo(9_000);
            assertThat(result.policyName()).isEqualTo("VIP_FIX_1000");
        }

        @Test
        @DisplayName("원가가 할인 금액보다 작으면 최종 금액은 0원")
        void priceUnderDiscount() {
            DiscountResult result = discountService.applyGradeDiscount(MemberGrade.VIP, 500);

            assertThat(result.finalPrice()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("VVIP 등급 - 10% 비율 할인")
    class VVipGrade {

        @Test
        @DisplayName("10000원 주문 시 10% = 1000원 할인 → 최종 9000원")
        void rate10Discount() {
            DiscountResult result = discountService.applyGradeDiscount(MemberGrade.VVIP, 10_000);

            assertThat(result.discountAmount()).isEqualTo(1_000);
            assertThat(result.finalPrice()).isEqualTo(9_000);
            assertThat(result.policyName()).isEqualTo("VVIP_RATE_10");
        }
    }
}
