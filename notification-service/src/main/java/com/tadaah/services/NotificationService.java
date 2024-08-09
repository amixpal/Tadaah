package com.tadaah.services;


import com.tadaah.models.Dto.request.NotificationDto;
import com.tadaah.models.Notifications;
import reactor.core.publisher.Mono;

public interface NotificationService {
  Mono<Notifications> createNotification(NotificationDto notificationDto);
}
