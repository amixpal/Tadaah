package com.tadaah.controllers;

import com.tadaah.models.Dto.request.UserDto;
import com.tadaah.models.Dto.request.UserFilterRequestDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.Dto.response.ResponseDto;
import com.tadaah.models.Users;
import com.tadaah.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/users")
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  /**
   * Create a new user.
   *
   * @param userDto The user to be created.
   * @return The created user.
   */
  @PostMapping
  public ResponseDto<Users> createUser(@Validated @RequestBody UserDto userDto) {
    logger.info("createUser API called with parameters: {}", userDto);
    Users createdUser = userService.createUser(userDto);
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
   * Get all users with pagination and optional filters.
   *
   * @param filterDto The filter criteria for retrieving users.
   * @return A paginated list of all users.
   */
  @PostMapping("/filter")
  public ResponseDto<PaginatedResponseDto<Users>> getAllUsers(@RequestBody UserFilterRequestDto filterDto) {
    logger.info("getAllUsers API called with filters - username: {}", filterDto.getUserName());
    Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize());
    PaginatedResponseDto<Users> users = userService.getAllUsers(filterDto.getUserName(), pageable);
    logger.info("Fetched {} users", users.getTotalElements());
    return ResponseDto.success(users);
  }

}
