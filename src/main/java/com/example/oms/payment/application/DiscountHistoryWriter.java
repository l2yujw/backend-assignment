package com.example.oms.payment.application;

import com.example.oms.core.annotation.Writer;
import com.example.oms.payment.domain.DiscountHistory;
import com.example.oms.payment.domain.repository.DiscountHistoryRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DiscountHistoryWriter {

    private final DiscountHistoryRepository discountHistoryRepository;

    public DiscountHistory save(DiscountHistory history) {
        return discountHistoryRepository.save(history);
    }
}
