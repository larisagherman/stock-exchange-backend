package com.stock_exchange_backend.stock_exchange_backend.services;

import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import com.stock_exchange_backend.stock_exchange_backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {

    private final OrderRepository orderRepository;

    public MatchingService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void process(Order order) {
        // Matching logic to be implemented
        orderRepository.deleteById(order.getId());
        System.out.println("Processed order with ID: " + order.getId());
    }
}
