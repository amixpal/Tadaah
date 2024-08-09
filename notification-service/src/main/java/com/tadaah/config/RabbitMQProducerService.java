package com.tadaah.config;

import com.tadaah.models.Dto.request.NotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducerService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "notifications.exchange";
    private static final String ROUTING_KEY = "notifications.routingkey";

    public void sendMessage(NotificationDto notificationDto) {
        logger.info("Sending message to RabbitMQ: {}", notificationDto);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, notificationDto);
    }
}
