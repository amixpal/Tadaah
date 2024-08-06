package com.tadaah.models;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
@Data
public class Notifications {
  @Id
  private String id;
  private LocalDateTime timestamp;
  private String receiver;
  private String documentId;
}
