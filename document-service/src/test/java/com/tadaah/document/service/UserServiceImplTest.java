package com.tadaah.document.service;

import com.tadaah.exceptions.UserServiceException;
import com.tadaah.models.Users;
import com.tadaah.models.Dto.request.UserDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.repositories.UserRepository;
import com.tadaah.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateUser_Success() {
    UserDto userDto = new UserDto();
    userDto.setUserName("testuser");
    userDto.setFirstName("Test");
    userDto.setLastName("User");

    Users user = new Users();
    user.setUserName(userDto.getUserName());
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());

    when(userRepository.findById(userDto.getUserName())).thenReturn(Optional.empty());
    when(userRepository.save(any(Users.class))).thenReturn(user);

    Users createdUser = userService.createUser(userDto);

    assertNotNull(createdUser);
    assertEquals(userDto.getUserName(), createdUser.getUserName());
    verify(userRepository, times(1)).findById(userDto.getUserName());
    verify(userRepository, times(1)).save(any(Users.class));
  }

  @Test
  public void testCreateUser_UserAlreadyExists() {
    UserDto userDto = new UserDto();
    userDto.setUserName("testuser");

    Users existingUser = new Users();
    existingUser.setUserName(userDto.getUserName());

    when(userRepository.findById(userDto.getUserName())).thenReturn(Optional.of(existingUser));

    UserServiceException exception = assertThrows(UserServiceException.class, () -> {
      userService.createUser(userDto);
    });

    assertEquals("User already exists with username: " + userDto.getUserName(), exception.getMessage());
    assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    verify(userRepository, times(1)).findById(userDto.getUserName());
    verify(userRepository, never()).save(any(Users.class));
  }

  @Test
  public void testDeleteUser_Success() {
    String username = "testuser";

    when(userRepository.existsById(username)).thenReturn(true);
    doNothing().when(userRepository).deleteById(username);

    assertDoesNotThrow(() -> userService.deleteUser(username));
    verify(userRepository, times(1)).existsById(username);
    verify(userRepository, times(1)).deleteById(username);
  }

  @Test
  public void testDeleteUser_UserNotFound() {
    String username = "testuser";

    when(userRepository.existsById(username)).thenReturn(false);

    UserServiceException exception = assertThrows(UserServiceException.class, () -> {
      userService.deleteUser(username);
    });

    assertEquals("User not found with username: " + username, exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    verify(userRepository, times(1)).existsById(username);
    verify(userRepository, never()).deleteById(username);
  }

  @Test
  public void testGetAllUsersWithPaginationAndFiltering_Success() {
    Users user1 = new Users();
    user1.setUserName("user1");

    Users user2 = new Users();
    user2.setUserName("user2");

    List<Users> userList = Arrays.asList(user1, user2);
    Page<Users> userPage = new PageImpl<>(userList);
    Pageable pageable = PageRequest.of(0, 10);

    when(userRepository.findByUsername("user", pageable)).thenReturn(userPage);

    PaginatedResponseDto<Users> result = userService.getAllUsers("user", pageable);

    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    verify(userRepository, times(1)).findByUsername("user", pageable);
  }
}
