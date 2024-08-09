package com.tadaah.models;

import io.swagger.v3.oas.annotations.media.Schema;

public enum NotificationType {

  @Schema(description = "Notification type for creating a new document")
  CREATE("{{userName}} has added a new document named {{documentName}}."),

  @Schema(description = "Notification type for updating an existing document")
  UPDATE("{{userName}} has updated the document named {{documentName}}."),

  @Schema(description = "Notification type for deleting a document")
  DELETE("{{userName}} has deleted the document named {{documentName}}.");

  private final String messageTemplate;

  NotificationType(String messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }
}
