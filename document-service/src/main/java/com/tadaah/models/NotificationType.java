package com.tadaah.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Types of notifications with message templates")
public enum NotificationType {

  @Schema(description = "Notification for document creation")
  CREATE("{{userName}} has added a new document named {{documentName}}."),

  @Schema(description = "Notification for document update")
  UPDATE("{{userName}} has updated the document named {{documentName}}."),

  @Schema(description = "Notification for document deletion")
  DELETE("{{userName}} has deleted the document named {{documentName}}.");

  private final String messageTemplate;

  NotificationType(String messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }
}
