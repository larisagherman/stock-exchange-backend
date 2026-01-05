package com.stock_exchange_backend.stock_exchange_backend.services;

import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import com.stock_exchange_backend.stock_exchange_backend.entity.OrderStatus;
import com.stock_exchange_backend.stock_exchange_backend.entity.OrderType;
import com.stock_exchange_backend.stock_exchange_backend.entity.Transaction;
import com.stock_exchange_backend.stock_exchange_backend.repository.OrderRepository;
import com.stock_exchange_backend.stock_exchange_backend.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MatchingService {

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;

    public MatchingService(OrderRepository orderRepository, TransactionRepository transactionRepository) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
    }

    public void process(Order o) {
        Optional<Order> savedOrderOpt = orderRepository.findById(o.getId());
        if (savedOrderOpt.isEmpty()) {
            return;
        }
        Order order = savedOrderOpt.get();
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        if (order.getType() == OrderType.SELLING) {
            processSellingOrder(order);
        } else if (order.getType() == OrderType.BUYING) {
            processBuyingOrder(order);
        }
    }

    @Transactional
    private void processSellingOrder(Order order) {
        List<Order> matchingBuyingOrders = orderRepository.findMatchingBuyingOrders(
                order.getTicker(),
                order.getTraderId(),
                order.getPricePerShare()
        );
        if (matchingBuyingOrders.isEmpty()) {
            order.setStatus(OrderStatus.ACTIVE);
            orderRepository.save(order);
            return;
        }

        for (Order buyOrder : matchingBuyingOrders) {
            if (order.getQuantity() <= 0) {
                break;
            }

            int tradableQuantity = Math.min(order.getQuantity(), buyOrder.getQuantity());
            order.setQuantity(order.getQuantity() - tradableQuantity);
            buyOrder.setQuantity(buyOrder.getQuantity() - tradableQuantity);

            if (buyOrder.getQuantity() == 0) {
                buyOrder.setStatus(OrderStatus.COMPLETED);
            } else {
                buyOrder.setStatus(OrderStatus.ACTIVE);
            }

            orderRepository.save(buyOrder);

            Transaction transaction = new Transaction(
                    null,
                    buyOrder.getTraderId(),
                    order.getTraderId(),
                    order.getTicker(),
                    tradableQuantity,
                    buyOrder.getPricePerShare(),
                    Instant.now()
            );
            transactionRepository.save(transaction);
        }
        if (order.getQuantity() > 0) {
            order.setStatus(OrderStatus.ACTIVE);
        } else {
            order.setStatus(OrderStatus.COMPLETED);
        }
        orderRepository.save(order);
    }

    @Transactional
    private void processBuyingOrder(Order order) {
        List<Order> matchingSellingOrders = orderRepository.findMatchingSellingOrders(
                order.getTicker(),
                order.getTraderId(),
                order.getPricePerShare()
        );
        if (matchingSellingOrders.isEmpty()) {
            order.setStatus(OrderStatus.ACTIVE);
            orderRepository.save(order);
            return;
        }

        for (Order sellOrder : matchingSellingOrders) {
            if (order.getQuantity() <= 0) {
                break;
            }

            int tradableQuantity = Math.min(order.getQuantity(), sellOrder.getQuantity());
            order.setQuantity(order.getQuantity() - tradableQuantity);
            sellOrder.setQuantity(sellOrder.getQuantity() - tradableQuantity);

            if (sellOrder.getQuantity() == 0) {
                sellOrder.setStatus(OrderStatus.COMPLETED);
            } else {
                sellOrder.setStatus(OrderStatus.ACTIVE);
            }
            orderRepository.save(sellOrder);

            Transaction transaction = new Transaction(
                    null,
                    sellOrder.getTraderId(),
                    order.getTraderId(),
                    order.getTicker(),
                    tradableQuantity,
                    sellOrder.getPricePerShare(),
                    Instant.now()
            );
            transactionRepository.save(transaction);
        }

        if (order.getQuantity() > 0) {
            order.setStatus(OrderStatus.ACTIVE);
        } else {
            order.setStatus(OrderStatus.COMPLETED);
        }
        orderRepository.save(order);
    }
}
