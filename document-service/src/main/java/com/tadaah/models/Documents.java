package com.tadaah.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

/**
 * Represents a document in the documents collection.
 * Each document is associated with a user and contains metadata like name, type, and expiry date.
 */
@Document(collection = "documents")
@Data
public class Documents {
  @Id
  private String id;
  private String name;
  private String documentType;
  private String userName;
  private String fileUrl;
  private LocalDate expiryDate;
  private boolean verified;
  @JsonIgnore
  private String notificationError;  // To store notification error message

  @Override
  public String toString() {
    return "Documents{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", documentType='" + documentType + '\'' +
        ", userName='" + userName + '\'' +
        ", fileUrl=" + fileUrl +
        ", expiryDate=" + expiryDate +
        ", verified=" + verified +
        ", notificationError='" + notificationError + '\'' +
        '}';
  }
}
