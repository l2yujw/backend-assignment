package com.example.oms.payment.infra;

import com.example.oms.payment.domain.DiscountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaDiscountHistoryRepository extends JpaRepository<DiscountHistory, Long> {
    List<DiscountHistory> findByPaymentId(Long paymentId);
}
