package com.tadaah.controllers;

import com.tadaah.models.Dto.response.ResponseDto;
import com.tadaah.models.Users;
import com.tadaah.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  public ResponseDto<Users> createUser(@RequestBody Users user) {
    logger.info("createUser API called with parameters: {}", user);
    Users createdUser = userService.createUser(user);
    logger.info("User created successfully: {}", createdUser);
    return ResponseDto.success(createdUser);
  }

  /**
   * Delete a user.
   *
   * @param username The username of the user to be deleted.
   * @return No content.
   */
  @DeleteMapping("/{username}")
  public ResponseDto<Void> deleteUser(@PathVariable String username) {
    logger.info("deleteUser API called with username: {}", username);
    userService.deleteUser(username);
    logger.info("User deleted successfully with username: {}", username);
    return ResponseDto.success(null);
  }

  /**
   * Get all users.
   *
   * @return A list of all users.
   */
  @GetMapping
  public ResponseDto<List<Users>> getAllUsers() {
    logger.info("getAllUsers API called");
    List<Users> users = userService.getAllUsers();
    logger.info("Fetched {} users", users.size());
    return ResponseDto.success(users);
  }
}
