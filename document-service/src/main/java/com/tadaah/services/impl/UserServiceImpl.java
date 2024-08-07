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

  @Override
  public Users createUser(Users user) {
    logger.info("Creating a new user: {}", user.getUserName());
    Optional<Users> existingUser = userRepository.findById(user.getUserName());
    if (existingUser.isPresent()) {
      logger.warn("User already exists with username: {}", user.getUserName());
      throw new UserServiceException("User already exists with username: " + user.getUserName(), HttpStatus.CONFLICT);
    }
    try {
      return userRepository.save(user);
    } catch (Exception e) {
      logger.error("Error creating user: {}", user.getUserName(), e);
      throw new UserServiceException("Error creating user", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void deleteUser(String username) {
    logger.info("Deleting user with username: {}", username);
    if (!userRepository.existsById(username)) {
      logger.warn("User not found with username: {}", username);
      throw new UserServiceException("User not found with username: " + username, HttpStatus.NOT_FOUND);
    }
    try {
      userRepository.deleteById(username);
    } catch (Exception e) {
      logger.error("Error deleting user with username: {}", username, e);
      throw new UserServiceException("Error deleting user", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public List<Users> getAllUsers() {
    try {
      logger.info("Fetching all users");
      return userRepository.findAll();
    } catch (Exception e) {
      logger.error("Error fetching all users", e);
      throw new UserServiceException("Error fetching users", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
