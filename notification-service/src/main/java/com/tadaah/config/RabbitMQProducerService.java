package com.tadaah.config;

import com.tadaah.models.Dto.request.NotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for producing and sending messages to RabbitMQ.
 * This class is responsible for sending notification messages to the specified RabbitMQ exchange.
 */
@Service
public class RabbitMQProducerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducerService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "notifications.exchange";
    private static final String ROUTING_KEY = "notifications.routingkey";

    /**
     * Sends a notification message to the RabbitMQ exchange.
     *
     * @param notificationDto the notification data transfer object to be sent
     */
    public void sendMessage(NotificationDto notificationDto) {
        logger.info("Sending message to RabbitMQ: {}", notificationDto);

        try {
            // Send the notification message to the RabbitMQ exchange with the specified routing key
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, notificationDto);
            logger.info("Message sent successfully to exchange '{}' with routing key '{}'", EXCHANGE_NAME, ROUTING_KEY);
        } catch (Exception e) {
            // Log the exception if the message sending fails
            logger.error("Failed to send message to RabbitMQ", e);
        }
    }
}
