package com.example.oms.order.domain;

import com.example.oms.core.annotation.AggregateRoot;
import com.example.oms.core.guard.Guard;
import com.example.oms.core.persistence.AbstractTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AggregateRoot
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends AbstractTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "original_price", nullable = false)
    private int originalPrice;

    private Order(Long memberId, String productName, int originalPrice) {
        this.memberId = Guard.notNull(memberId, "memberId");
        this.productName = Guard.notBlank(productName, "productName");
        this.originalPrice = Guard.minValue(originalPrice, 1, "originalPrice");
    }

    public static Order place(Long memberId, String productName, int originalPrice) {
        return new Order(memberId, productName, originalPrice);
    }
}
