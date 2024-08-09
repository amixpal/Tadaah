package com.tadaah.services.impl;

import com.tadaah.exception.NotificationServiceException;
import com.tadaah.models.Dto.request.NotificationDto;
import com.tadaah.models.Notifications;
import com.tadaah.repositories.NotificationRepository;
import com.tadaah.services.NotificationService;
import com.tadaah.utils.GenericUtils;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Creates a new notification from the provided {@link NotificationDto}.
     *
     * <p>This method logs the incoming payload, converts it to a {@link Notifications} entity,
     * sets the current timestamp, and saves it to the repository. It logs the success or failure of the operation.
     * After successfully saving, the notification is emitted to the SSE stream.
     *
     * @param notificationDto The data for the new notification.
     * @return A Mono emitting the created {@link Notifications} entity.
     * @throws NotificationServiceException if an error occurs during creation.
     */
    @Override
    public Mono<Notifications> createNotification(NotificationDto notificationDto) {
        log.info("Creating notification with payload: {}", notificationDto);

        Notifications notification = new Notifications();
        GenericUtils.mergeObjects(notification, notificationDto);
        notification.setTimestamp(LocalDateTime.now());

        return notificationRepository.save(notification)
                .doOnSuccess(createdNotification -> {
                    log.info("Notification created successfully with ID: {}", createdNotification.getId());
                })
                .doOnError(e -> {
                    log.error("Error creating notification with payload: {}", notificationDto, e);
                    throw new NotificationServiceException("Failed to create notification", HttpStatus.INTERNAL_SERVER_ERROR, e);
                });
    }
}
