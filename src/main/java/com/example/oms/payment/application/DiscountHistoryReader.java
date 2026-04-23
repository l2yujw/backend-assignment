package com.example.oms.payment.application;

import com.example.oms.core.annotation.Reader;
import com.example.oms.payment.domain.DiscountHistory;
import com.example.oms.payment.domain.repository.DiscountHistoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class DiscountHistoryReader {

    private final DiscountHistoryRepository discountHistoryRepository;

    public List<DiscountHistory> getByPaymentId(Long paymentId) {
        return discountHistoryRepository.findByPaymentId(paymentId);
    }
}
