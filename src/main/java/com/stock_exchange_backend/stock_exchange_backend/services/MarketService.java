package com.stock_exchange_backend.stock_exchange_backend.services;

import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import com.stock_exchange_backend.stock_exchange_backend.entity.OrderStatus;
import com.stock_exchange_backend.stock_exchange_backend.queue.RabbitMQProducer;
import com.stock_exchange_backend.stock_exchange_backend.repository.OrderRepository;
import com.stock_exchange_backend.stock_exchange_backend.request.OrderRequest;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class MarketService {

    private final OrderRepository orderRepository;
    private final RabbitMQProducer producer;

    public MarketService(OrderRepository orderRepository, RabbitMQProducer producer) {
        this.orderRepository = orderRepository;
        this.producer = producer;
    }

    public ResponseEntity<Map<String, String>> placeOrder(@NonNull OrderRequest orderRequest) {
        // Implementation for placing an order
        Order order = new Order(
                null,
                orderRequest.getUserId(),
                orderRequest.getTicker(),
                orderRequest.getQuantity(),
                orderRequest.getPricePerShare(),
                orderRequest.getType(),
                OrderStatus.ACTIVE,
                Instant.now()
        );

        orderRepository.save(order);
        producer.sendMessage(order);

        Long id = order.getId();
        Map<String, String> body = Map.of("orderId", id.toString());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(body);
    }

    public ResponseEntity<Map<String, String>> orderStatus(@NonNull Long orderId) {
        // Implementation for checking order status
        return orderRepository.findById(orderId)
                .map(order -> {
                    Map<String, String> body = Map.of("status", order.getStatus().toString());
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(body);
                })
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
