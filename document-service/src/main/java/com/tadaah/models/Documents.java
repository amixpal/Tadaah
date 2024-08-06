package com.tadaah.models;

import java.util.Arrays;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "documents")
@Data
public class Documents {
  @Id
  private String id;
  private String name;
  private String documentType;
  private String user;
  private byte[] file;
  private LocalDate expiryDate;
  private boolean verified;

  @Override
  public String toString() {
    return "Documents{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", documentType='" + documentType + '\'' +
        ", user='" + user + '\'' +
        ", file=" + Arrays.toString(file) +
        ", expiryDate=" + expiryDate +
        ", verified=" + verified +
        '}';
  }
}
