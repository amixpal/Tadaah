package com.tadaah.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
@Data
@Schema(description = "Represents a notification document in the system.")
public class Notifications {

  @Id
  @Schema(description = "The unique identifier of the notification", example = "60c72b2f9b1e8e1b8c1d4f2a")
  private String id;

  @Schema(description = "The timestamp when the notification was created", example = "2024-08-09T14:00:00Z")
  private LocalDateTime timestamp;

  @Schema(description = "The receiver of the notification", example = "john.doe@example.com")
  private String receiver;

  @Schema(description = "The unique identifier of the document", example = "abc123")
  private String documentId;

  @Schema(description = "The name of the document", example = "Invoice 2024")
  private String documentName;

  @Schema(description = "The type of the notification event", example = "CREATE")
  private NotificationType eventType;

  @Schema(description = "The message content of the notification", example = "Your document has been updated.")
  private String message;
}
