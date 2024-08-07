package com.tadaah.utils;

import com.tadaah.exceptions.NotificationServiceException;
import com.tadaah.models.Documents;
import com.tadaah.models.NotificationType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class NotificationUtil {

  private NotificationUtil() {
    // Private constructor to prevent instantiation
  }

  /**
   * Sends a notification to the notification service.
   *
   * @param restTemplate           The RestTemplate to use for sending the notification.
   * @param notificationServiceUrl The URL of the notification service.
   * @param documents              The document object
   * @param notificationType       The type of notification (CREATE, UPDATE, DELETE).
   */
  public static void sendNotification(RestTemplate restTemplate, String notificationServiceUrl,
      Documents documents, NotificationType notificationType) {
    LocalDateTime now = LocalDateTime.now();
    // Format the timestamp in a human-readable format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String timestamp = now.format(formatter);

    String message = notificationType.getMessageTemplate()
        .replace("{{userName}}", documents.getUserName())
        .replace("{{documentName}}", documents.getName());

    NotificationRequest request = new NotificationRequest(
        documents.getUserName(),
        documents.getName(),
        timestamp,
        notificationType.name(),
        message,
        documents.getId()
    );

    try {
      restTemplate.postForEntity(notificationServiceUrl, request, Void.class);
      log.info("Notification sent for action: {} on document named: {}", notificationType,
          documents.getName());
    } catch (Exception e) {
      throw new NotificationServiceException(
          "Failed to send notification for action: " + notificationType + " on document named: "
              + documents.getName(), HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  @Data
  public static class NotificationRequest {

    private String receiver;
    private String documentName;
    private String documentId;
    private String timestamp;
    private String eventType;
    private String message;

    public NotificationRequest(String receiver, String documentName, String timestamp,
        String eventType, String message, String documentId) {
      this.receiver = receiver;
      this.documentName = documentName;
      this.timestamp = timestamp;
      this.eventType = eventType;
      this.message = message;
      this.documentId = documentId;
    }
  }
}
