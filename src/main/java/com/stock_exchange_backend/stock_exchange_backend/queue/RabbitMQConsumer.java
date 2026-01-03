package com.stock_exchange_backend.stock_exchange_backend.queue;

import com.stock_exchange_backend.stock_exchange_backend.config.RabbitMQConfig;
import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import com.stock_exchange_backend.stock_exchange_backend.services.MatchingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {
    private final MatchingService matchingService;

    RabbitMQConsumer (MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveOrder(Order order) {
        matchingService.process(order);
    }
}
