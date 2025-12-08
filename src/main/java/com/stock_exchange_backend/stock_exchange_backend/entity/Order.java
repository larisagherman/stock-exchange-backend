package com.stock_exchange_backend.stock_exchange_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int traderId;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double pricePerShare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType type;  // BUYING or SELLING

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;  // e.g. ACTIVE, UNGOING

    @Column(nullable = false)
    private int sharesRemaining;

    @Column(nullable = false)
    private Instant createdAt;

}

