package com.tadaah.services.impl;

import com.tadaah.exceptions.UserServiceException;
import com.tadaah.models.Dto.request.UserDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.Users;
import com.tadaah.repositories.UserRepository;
import com.tadaah.services.UserService;
import com.tadaah.utils.GenericUtils;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  /**
   * Creates a new user.
   *
   * @param userDto The user data transfer object containing user details.
   * @return The created user.
   * @throws UserServiceException if a user with the given username already exists or unexpected errors occur during creation.
   */
  @Override
  public Users createUser(UserDto userDto) {
    log.info("Creating a new user: {}", userDto.getUserName());

    // Check if a user with the same username already exists
    Optional<Users> existingUser = userRepository.findById(userDto.getUserName());
    if (existingUser.isPresent()) {
      log.warn("User already exists with username: {}", userDto.getUserName());
      throw new UserServiceException("User already exists with username: " + userDto.getUserName(), HttpStatus.CONFLICT);
    }

    try {
      // Convert UserDto to Users entity using the generic utility method
      Users user = new Users();
      GenericUtils.mergeObjects(user, userDto);

      // Save the new user to the database
      return userRepository.save(user);
    } catch (RuntimeException e) {
      log.error("Error creating user: {}", userDto.getUserName(), e);
      throw new UserServiceException("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Deletes a user.
   *
   * @param username The username of the user to be deleted.
   * @throws UserServiceException if the user is not found or unexpected errors occur during deletion.
   */
  @Override
  public void deleteUser(String username) {
    log.info("Deleting user with username: {}", username);

    // Check if the user exists by username
    if (!userRepository.existsById(username)) {
      log.warn("User not found with username: {}", username);
      throw new UserServiceException("User not found with username: " + username, HttpStatus.NOT_FOUND);
    }

    try {
      // Delete the user from the database
      userRepository.deleteById(username);
    } catch (RuntimeException e) {
      log.error("Error deleting user with username: {}", username, e);
      throw new UserServiceException("Error deleting user", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Retrieves all users with optional filtering by username and email, and supports pagination.
   *
   * This method fetches users from the database with the specified filters and pagination settings.
   * If no filters are provided, it retrieves all users. The results are returned in a paginated format.
   *
   * @param username The username to filter by (optional).
   * @param pageable The pagination information (page number and page size).
   * @return A UserFilterResponseDto containing the paginated list of users and pagination details.
   * @throws UserServiceException if an unexpected error occurs during retrieval.
   */
  @Override
  public PaginatedResponseDto<Users> getAllUsers(String username, Pageable pageable) {
    try {
      log.info("Fetching all users with filters - username: {}", username);
      // Retrieve all users from the database with pagination and filters
      Page<Users> usersPage = userRepository.findByUsername(username, pageable);
      return new PaginatedResponseDto<>(usersPage);
    } catch (RuntimeException e) {
      log.error("Error fetching all users", e);
      throw new UserServiceException("Error fetching users", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

}

