package com.tadaah.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "documents")
@Schema(description = "Represents a document with metadata such as name, type, and expiry date.")
public class Documents {

  @Id
  @Schema(description = "The unique identifier for the document", example = "60b6a78d2f2d4a3c0f3aef12")
  @JsonProperty("id")
  private String id;

  @Schema(description = "The name of the document", example = "Passport")
  private String name;

  @Schema(description = "The type of the document", allowableValues = "FINANCIAL_DOCUMENT, ID_VERIFICATION, LEGAL_DOCUMENT, OTHER")
  private DocumentType documentType;

  @Schema(description = "The username of the document owner", example = "john_doe")
  private String userName;

  @Schema(description = "The URL where the document can be accessed", example = "http://example.com/document.pdf")
  private String fileUrl;

  @Schema(description = "The expiry date of the document", example = "2024-12-31")
  private LocalDate expiryDate;

  @Schema(description = "Indicates whether the document is verified", example = "true")
  private boolean verified;

  @Schema(description = "Error message related to notifications", example = "Failed to send notification")
  @JsonIgnore
  private String notificationError;

  // Setter for ObjectId, converts to string
  public void setId(ObjectId id) {
    this.id = id.toHexString();
  }

  @Override
  public String toString() {
    return "Documents{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", documentType=" + documentType +
        ", userName='" + userName + '\'' +
        ", fileUrl='" + fileUrl + '\'' +
        ", expiryDate=" + expiryDate +
        ", verified=" + verified +
        ", notificationError='" + notificationError + '\'' +
        '}';
  }
}
