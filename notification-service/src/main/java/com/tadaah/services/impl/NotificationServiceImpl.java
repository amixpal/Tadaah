package com.tadaah.services.impl;

import com.tadaah.exception.NotificationServiceException;
import com.tadaah.models.Notifications;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.Dto.request.NotificationDto;
import com.tadaah.repositories.NotificationRepository;
import com.tadaah.services.NotificationService;
import com.tadaah.utils.GenericUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
  private final Sinks.Many<Notifications> notificationSink = Sinks.many().multicast().onBackpressureBuffer();

  @Autowired
  private NotificationRepository notificationRepository;

  /**
   * Creates a new notification from the provided {@link NotificationDto}.
   *
   * <p>This method logs the incoming payload, converts it to a {@link Notifications} entity,
   * sets the current timestamp, and saves it to the repository. It logs the success or failure of the operation.
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
        .doOnSuccess(createdNotification -> log.info("Notification created successfully with ID: {}", createdNotification.getId()))
        .doOnError(e -> {
          log.error("Error creating notification with payload: {}", notificationDto, e);
          throw new NotificationServiceException("Failed to create notification", HttpStatus.INTERNAL_SERVER_ERROR, e);
        });
  }

  /**
   * Retrieves notifications with optional filtering and pagination.
   *
   * <p>This method fetches notifications from the repository based on the provided filters and pagination information.
   * It logs the retrieval process and wraps the result in a {@link PaginatedResponseDto}.
   *
   * @param notificationType The type of notification to filter by.
   * @param userName The user name to filter by.
   * @param documentName The document name to filter by.
   * @param pageable The pagination information.
   * @return A Mono emitting a {@link PaginatedResponseDto} containing notifications that match the filter criteria.
   * @throws NotificationServiceException if unexpected errors occur during retrieval.
   */
  @Override
  public Mono<PaginatedResponseDto<Notifications>> getNotifications(String notificationType, String userName, String documentName, Pageable pageable) {
    log.info("Fetching notifications with filters - type: {}, userName: {}, documentName: {}", notificationType, userName, documentName);
    return notificationRepository.findByFilters(notificationType, userName, documentName)
        .collectList()
        .map(notifications -> {
          long totalElements = notifications.size();
          int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
          boolean isFirst = pageable.getPageNumber() == 0;
          boolean isLast = pageable.getPageNumber() == totalPages - 1;
          boolean isEmpty = notifications.isEmpty();
          return new PaginatedResponseDto<>(notifications, totalElements, totalPages, pageable.getPageSize(), isFirst, isLast, isEmpty);
        })
        .doOnError(e -> {
          log.error("Error fetching notifications with filters - type: {}, userName: {}, documentName: {}", notificationType, userName, documentName, e);
          throw new NotificationServiceException("Error fetching notifications with filters", HttpStatus.INTERNAL_SERVER_ERROR, e);
        });
  }

  /**
   * Streams all notifications in real-time using Server-Sent Events (SSE).
   *
   * <p>This method returns a Flux that emits notifications as they are created. The connection remains open,
   * allowing clients to receive real-time updates.
   *
   * @return A Flux emitting {@link Notifications} entities in real-time.
   */
  @Override
  public Flux<Notifications> streamNotifications() {
    log.info("Streaming all notifications");
    return notificationSink.asFlux();
  }

  /**
   * Emits a notification to the stream.
   *
   * <p>This method pushes the given notification to the SSE stream, making it available to subscribed clients.
   *
   * @param notification The {@link Notifications} entity to emit.
   */
  @Override
  public void emitNotification(Notifications notification) {
    notificationSink.tryEmitNext(notification);
  }
}

