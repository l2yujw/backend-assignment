package com.example.oms.discount.infra;

import com.example.oms.discount.domain.DiscountPolicy;
import com.example.oms.discount.domain.NormalDiscountPolicy;
import com.example.oms.discount.domain.VipDiscountPolicy;
import com.example.oms.discount.domain.VvipDiscountPolicy;
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
}
