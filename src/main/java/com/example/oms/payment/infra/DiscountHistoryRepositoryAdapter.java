package com.example.oms.payment.infra;

import com.example.oms.payment.domain.DiscountHistory;
import com.example.oms.payment.domain.repository.DiscountHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiscountHistoryRepositoryAdapter implements DiscountHistoryRepository {

    private final JpaDiscountHistoryRepository jpa;

    @Override
    public DiscountHistory save(DiscountHistory discountHistory) {
        return jpa.save(discountHistory);
    }

    @Override
    public List<DiscountHistory> findByPaymentId(Long paymentId) {
        return jpa.findByPaymentId(paymentId);
    }
}
