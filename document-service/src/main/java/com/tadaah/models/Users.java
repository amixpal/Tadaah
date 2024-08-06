package com.tadaah.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class Users {
  @Id
  private String username;
  private String firstName;
  private String lastName;
  private String email;

  @Override
  public String toString() {
    return "Users{" +
        "username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        '}';
  }
}
