package com.tadaah.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
@Schema(description = "Represents a user with personal information and timestamps.")
public class Users {

  @Id
  @Schema(description = "The unique username for the user", example = "john_doe")
  private String userName;

  @Schema(description = "The first name of the user", example = "John")
  private String firstName;

  @Schema(description = "The last name of the user", example = "Doe")
  private String lastName;

  @CreatedDate
  @Schema(description = "The date and time when the user was created", example = "2024-08-09T10:15:30")
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Schema(description = "The date and time when the user was last modified", example = "2024-08-09T10:15:30")
  private LocalDateTime lastModifiedDate;

  @Override
  public String toString() {
    return "Users{" +
        "userName='" + userName + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", createdDate=" + createdDate +
        ", lastModifiedDate=" + lastModifiedDate +
        '}';
  }
}
