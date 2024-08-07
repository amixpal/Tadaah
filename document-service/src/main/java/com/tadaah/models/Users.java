package com.tadaah.models;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user in the users collection.
 * Each user has a unique username and contains personal information such as first name, last name, and email.
 */
@Document(collection = "users")
@Data
public class Users {
  @Id
  private String userName;
  private String firstName;
  private String lastName;

  // TODO: Bug, Not getting updated
  @CreatedDate
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime lastModifiedDate;

  @Override
  public String toString() {
    return "Users{" +
        "username='" + userName + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", createdDate=" + createdDate +
        ", lastModifiedDate=" + lastModifiedDate +
        '}';
  }
}
