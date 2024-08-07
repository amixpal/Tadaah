package com.tadaah.services;


import com.tadaah.models.Notifications;
import java.util.List;

public interface NotificationService {
  Notifications createNotification(Notifications notification);
  List<Notifications> getAllNotifications();
}
