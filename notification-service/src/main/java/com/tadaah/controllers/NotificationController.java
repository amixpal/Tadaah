package com.tadaah.controllers;

import com.tadaah.models.Dto.request.NotificationFilterRequestDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.Dto.response.ResponseDto;
import com.tadaah.models.Dto.request.NotificationDto;
import com.tadaah.models.Notifications;
import com.tadaah.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/notifications")
public class NotificationController {

  private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

  @Autowired
  private NotificationService notificationService;

  /**
   * Send a new notification
   *
   * @param notificationDto New Notification will send.
   * @return The created notification.
   */
  @PostMapping
  public Mono<ResponseDto<Notifications>> sendNotification(@RequestBody NotificationDto notificationDto) {
    logger.info("createNotification API called payload: {}", notificationDto);
    return notificationService.createNotification(notificationDto)
        .map(ResponseDto::success);
  }

  /**
   * Retrieves notifications based on the provided filters and pagination information.
   *
   * @param filter The filter criteria including notificationType, userName, documentName, page, and size.
   * @return A ResponseDto containing a PaginatedResponseDto with the filtered notifications and pagination details.
   */
  @PostMapping("/filter")
  public Mono<ResponseDto<PaginatedResponseDto<Notifications>>> getNotifications(@RequestBody NotificationFilterRequestDto filter) {
    logger.info("Fetching notifications with filters - type: {}, userName: {}, documentName: {}",
        filter.getNotificationType(), filter.getUserName(), filter.getDocumentName());
    Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
    return notificationService.getNotifications(
        filter.getNotificationType(),
        filter.getUserName(),
        filter.getDocumentName(),
        pageable
    ).map(ResponseDto::success);
  }

  /**
   * SSE endpoint for receiving real-time notifications.
   *
   * @return Flux for server-sent events.
   */
  @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Notifications> streamNotifications() {
    logger.info("Streaming all notifications");
    return notificationService.streamNotifications();
  }
}
