package com.stock_exchange_backend.stock_exchange_backend.queue;

import com.stock_exchange_backend.stock_exchange_backend.config.RabbitMQConfig;
import com.stock_exchange_backend.stock_exchange_backend.entity.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Order order) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "routing.key", order);
    }
}
