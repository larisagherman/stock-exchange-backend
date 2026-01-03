package com.stock_exchange_backend.stock_exchange_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String traderId;

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
    private OrderStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", traderId=" + traderId +
                ", ticker='" + ticker + '\'' +
                ", quantity=" + quantity +
                ", pricePerShare=" + pricePerShare +
                ", type=" + type +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}

