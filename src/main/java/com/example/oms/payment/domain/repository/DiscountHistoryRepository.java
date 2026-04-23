package com.example.oms.payment.domain.repository;

import com.example.oms.payment.domain.DiscountHistory;

import java.util.List;

public interface DiscountHistoryRepository {
    DiscountHistory save(DiscountHistory history);
    List<DiscountHistory> findByPaymentId(Long paymentId);
}
