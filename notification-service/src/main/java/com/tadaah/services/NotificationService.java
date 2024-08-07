package com.tadaah.services;


import com.tadaah.models.Dto.request.NotificationDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.Notifications;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationService {
  Mono<Notifications> createNotification(NotificationDto notificationDto);
  Mono<PaginatedResponseDto<Notifications>> getNotifications(String notificationType, String userName, String documentName, Pageable pageable);
  Flux<Notifications> streamNotifications();
  void emitNotification(Notifications notification);
}
