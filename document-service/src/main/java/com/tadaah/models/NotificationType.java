package com.tadaah.models;

public enum NotificationType {
  CREATE("{{userName}} has added a new document named {{documentName}}."),
  UPDATE("{{userName}} has updated the document named {{documentName}}."),
  DELETE("{{userName}} has deleted the document named {{documentName}}.");

  private final String messageTemplate;

  NotificationType(String messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }
}
