package com.tadaah.services.impl;

import com.tadaah.exceptions.UserServiceException;
import com.tadaah.models.Users;
import com.tadaah.repositories.UserRepository;
import com.tadaah.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  /**
   * Creates a new user.
   *
   * @param user The user to be created.
   * @return The created user.
   * @throws UserServiceException if a user with the given username already exists or unexpected errors occur during creation.
   */
  @Override
  public Users createUser(Users user) {
    logger.info("Creating a new user: {}", user.getUserName());

    // Check if a user with the same username already exists
    Optional<Users> existingUser = userRepository.findById(user.getUserName());
    if (existingUser.isPresent()) {
      logger.warn("User already exists with username: {}", user.getUserName());
      throw new UserServiceException("User already exists with username: " + user.getUserName(), HttpStatus.CONFLICT);
    }

    try {
      // Save the new user to the database
      return userRepository.save(user);
    } catch (RuntimeException e) {
      logger.error("Error creating user: {}", user.getUserName(), e);
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
    logger.info("Deleting user with username: {}", username);

    // Check if the user exists by username
    if (!userRepository.existsById(username)) {
      logger.warn("User not found with username: {}", username);
      throw new UserServiceException("User not found with username: " + username, HttpStatus.NOT_FOUND);
    }

    try {
      // Delete the user from the database
      userRepository.deleteById(username);
    } catch (RuntimeException e) {
      logger.error("Error deleting user with username: {}", username, e);
      throw new UserServiceException("Error deleting user", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }

  /**
   * Retrieves all users.
   *
   * @return A list of all users.
   * @throws UserServiceException if unexpected errors occur during retrieval.
   */
  @Override
  public List<Users> getAllUsers() {
    try {
      logger.info("Fetching all users");
      // Retrieve all users from the database
      return userRepository.findAll();
    } catch (RuntimeException e) {
      logger.error("Error fetching all users", e);
      throw new UserServiceException("Error fetching users", HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
  }
}

