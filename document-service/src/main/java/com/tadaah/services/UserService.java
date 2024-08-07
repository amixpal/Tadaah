package com.tadaah.services;

import com.tadaah.models.Dto.request.UserDto;
import com.tadaah.models.Dto.response.PaginatedResponseDto;
import com.tadaah.models.Users;
import org.springframework.data.domain.Pageable;

public interface UserService {
  Users createUser(UserDto user);
  void deleteUser(String username);
  PaginatedResponseDto<Users> getAllUsers(String username, Pageable pageable);
}
