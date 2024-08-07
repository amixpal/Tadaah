package com.tadaah.models.Dto.request;

import lombok.Data;

@Data
public class NotificationFilterRequestDto {
  private String notificationType;
  private String userName;
  private String documentName;
  private int page;
  private int size;
}

