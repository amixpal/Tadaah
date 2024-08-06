package com.tadaah.services.impl;

import com.tadaah.models.Users;
import com.tadaah.repositories.UserRepository;
import com.tadaah.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public Users createUser(Users user) {
    return userRepository.save(user);
  }

  @Override
  public Users updateUser(String username, Users user) {
    user.setUsername(username);
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(String username) {
    userRepository.deleteById(username);
  }

  @Override
  public List<Users> getAllUsers() {
    return userRepository.findAll();
  }
}
