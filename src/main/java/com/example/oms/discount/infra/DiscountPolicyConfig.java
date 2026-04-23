package com.example.oms.discount.infra;

import com.example.oms.discount.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class DiscountPolicyConfig {

    @Bean
    public List<DiscountPolicy> discountPolicies() {
        return List.of(
                new NormalDiscountPolicy(),
                new VipDiscountPolicy(),
                new VvipDiscountPolicy()
        );
    }

    @Bean
    public List<PaymentMethodDiscountPolicy> paymentMethodDiscountPolicies() {
        return List.of(
                new PointPaymentDiscountPolicy()
        );
    }
}
