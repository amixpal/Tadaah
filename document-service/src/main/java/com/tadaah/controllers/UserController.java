package com.tadaah.controllers;

import com.tadaah.models.Users;
import com.tadaah.services.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  /**
   * Create a new user.
   *
   * @param user The user to be created.
   * @return The created user.
   */
  @PostMapping
  public ResponseEntity<Users> createUser(@RequestBody Users user) {
    logger.info("createUser API called with parameters: {}", user);
    Users createdUser = userService.createUser(user);
    logger.info("User created successfully: {}", createdUser);
    return ResponseEntity.status(201).body(createdUser);
  }

  /**
   * Delete a user.
   *
   * @param username The username of the user to be deleted.
   * @return No content.
   */
  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    logger.info("deleteUser API called with username: {}", username);
    userService.deleteUser(username);
    logger.info("User deleted successfully with username: {}", username);
    return ResponseEntity.noContent().build();
  }

  /**
   * Get all users.
   *
   * @return A list of all users.
   */
  @GetMapping
  public ResponseEntity<List<Users>> getAllUsers() {
    logger.info("getAllUsers API called");
    List<Users> users = userService.getAllUsers();
    logger.info("Fetched {} users", users.size());
    return ResponseEntity.ok(users);
  }
}
