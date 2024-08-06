package com.tadaah.controllers;

import com.tadaah.models.Notifications;
import com.tadaah.services.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
  @Autowired
  private NotificationService notificationService;

  @PostMapping
  public ResponseEntity<Notifications> createNotification(@RequestBody Notifications notification) {
    Notifications createdNotification = notificationService.createNotification(notification);
    return ResponseEntity.status(201).body(createdNotification);
  }

  @GetMapping
  public ResponseEntity<List<Notifications>> getAllNotifications() {
    List<Notifications> notifications = notificationService.getAllNotifications();
    return ResponseEntity.ok(notifications);
  }
}
