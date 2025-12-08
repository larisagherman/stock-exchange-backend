package com.stock_exchange_backend.stock_exchange_backend.repository;

import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
