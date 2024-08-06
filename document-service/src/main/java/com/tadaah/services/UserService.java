package com.tadaah.services;

import com.tadaah.models.Users;
import java.util.List;

public interface UserService {
  Users createUser(Users user);
  Users updateUser(String username, Users user);
  void deleteUser(String username);
  List<Users> getAllUsers();
}
