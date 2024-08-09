package com.tadaah.config;

import com.tadaah.models.Dto.request.NotificationDto;
import com.tadaah.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumerService.class);

    private final NotificationService notificationService;

    public RabbitMQConsumerService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "notifications.queue")
    public void consumeMessage(NotificationDto notificationDto) {
        logger.info("Consumed message from RabbitMQ: {}", notificationDto);

        // Save the notification to the database and then emit it
        notificationService.createNotification(notificationDto).subscribe(notificationService::emitNotification);
    }
}
