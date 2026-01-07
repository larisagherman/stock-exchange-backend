package com.stock_exchange_backend.stock_exchange_backend.services;

import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import com.stock_exchange_backend.stock_exchange_backend.entity.OrderStatus;
import com.stock_exchange_backend.stock_exchange_backend.entity.Transaction;
import com.stock_exchange_backend.stock_exchange_backend.queue.RabbitMQProducer;
import com.stock_exchange_backend.stock_exchange_backend.repository.OrderRepository;
import com.stock_exchange_backend.stock_exchange_backend.repository.TransactionRepository;
import com.stock_exchange_backend.stock_exchange_backend.request.OrderRequest;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class MarketService {

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final RabbitMQProducer producer;

    public MarketService(OrderRepository orderRepository, TransactionRepository transactionRepository, RabbitMQProducer producer) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
        this.producer = producer;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllActiveOrders();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public ResponseEntity<Map<String, String>> placeOrder(@NonNull OrderRequest orderRequest) {
        Order order = new Order(
                null,
                orderRequest.getUserId(),
                orderRequest.getTicker(),
                orderRequest.getQuantity(),
                orderRequest.getPricePerShare(),
                orderRequest.getType(),
                OrderStatus.QUEUED,
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
                    Map<String, String> body = Map.of("status", order.getStatus().toString(),
                            "quantity", String.valueOf(order.getQuantity()));
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(body);
                })
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }
}
