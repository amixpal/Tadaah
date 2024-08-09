package com.tadaah.config;

import com.tadaah.models.Dto.request.NotificationDto;
import com.tadaah.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Service class for consuming messages from RabbitMQ and processing them.
 * This class listens to messages on the specified RabbitMQ queue and handles them accordingly.
 */
@Service
public class RabbitMQConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumerService.class);

    private final NotificationService notificationService;

    /**
     * Constructor for RabbitMQConsumerService.
     *
     * @param notificationService the service used for handling notifications
     */
    public RabbitMQConsumerService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Listens to messages from the RabbitMQ queue and processes them.
     *
     * @param notificationDto the notification data transfer object containing message details
     */
    @RabbitListener(queues = "notifications.queue")
    public void consumeMessage(NotificationDto notificationDto) {
        logger.info("Consumed message from RabbitMQ: {}", notificationDto);

        // Process the consumed message
        // Save the notification to the database and then emit it
        notificationService.createNotification(notificationDto)
            .doOnSuccess(notification -> logger.info("Notification saved successfully: {}", notification))
            .doOnError(error -> logger.error("Error saving notification: ", error))
            .subscribe();
    }
}
