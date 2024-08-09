package com.tadaah.models.Dto.request;

import com.tadaah.models.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for notification details.")
public class NotificationDto {

  @Schema(description = "The receiver of the notification", example = "john.doe@example.com")
  @NotNull(message = "Receiver is required")
  private String receiver;

  @Schema(description = "The name of the document associated with the notification", example = "Invoice 2024")
  @NotNull(message = "Document name is required")
  private String documentName;

  @Schema(description = "The unique identifier of the document", example = "abc123")
  @NotNull(message = "Document ID is required")
  private String documentId;

  @Schema(description = "The timestamp when the notification was created", example = "2024-08-09T14:00:00Z")
  @NotNull(message = "Timestamp is required")
  private LocalDateTime timestamp;

  @Schema(description = "The type of the notification event", example = "CREATE")
  @NotNull(message = "Event type is required")
  private NotificationType eventType;

  @Schema(description = "The message content of the notification", example = "Your document has been updated.")
  @NotBlank(message = "Message is required")
  private String message;
}