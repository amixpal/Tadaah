package com.tadaah.models.Dto.request;

import lombok.Data;

@Data
public class NotificationDto {
  private String receiver;
  private String documentName;
  private String documentId;
  private String timestamp;
  private String eventType;
  private String message;
}
