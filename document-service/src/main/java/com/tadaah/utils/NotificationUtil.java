package com.tadaah.utils;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class NotificationUtil {
  private static final Logger logger = LoggerFactory.getLogger(NotificationUtil.class);

  // Private constructor to prevent instantiation
  private NotificationUtil() {
  }

  /**
   * Sends a notification to the notification service.
   *
   * @param restTemplate The RestTemplate to use for sending the notification.
   * @param notificationServiceUrl The URL of the notification service.
   * @param receiver The receiver of the notification.
   * @param documentId The document ID related to the notification.
   * @param action The action performed (CREATED, UPDATED, DELETED).
   */

  //TODO: Add a custom message here as well
  public static void sendNotification(RestTemplate restTemplate, String notificationServiceUrl, String receiver, String documentId, String action) {
    NotificationRequest request = new NotificationRequest(receiver, documentId, action);
    restTemplate.postForEntity(notificationServiceUrl, request, Void.class);
    logger.info("Notification sent for action: {} on document ID: {}", action, documentId);
  }

  @Data
  public static class NotificationRequest {
    private String receiver;
    private String documentId;
    private String action;

    public NotificationRequest(String receiver, String documentId, String action) {
      this.receiver = receiver;
      this.documentId = documentId;
      this.action = action;
    }
  }
}
