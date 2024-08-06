package com.tadaah.services.impl;

import com.tadaah.models.Notifications;
import com.tadaah.repositories.NotificationRepository;
import com.tadaah.services.NotificationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
  @Autowired
  private NotificationRepository notificationRepository;

  @Override
  public Notifications createNotification(Notifications notification) {
    notification.setTimestamp(LocalDateTime.now());
    return notificationRepository.save(notification);
  }

  @Override
  public List<Notifications> getAllNotifications() {
    return notificationRepository.findAll();
  }
}
