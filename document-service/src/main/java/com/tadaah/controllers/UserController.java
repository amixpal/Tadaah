package com.tadaah.controllers;

import com.tadaah.models.Users;
import com.tadaah.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<Users> createUser(@RequestBody Users user) {
    Users createdUser = userService.createUser(user);
    return ResponseEntity.status(201).body(createdUser);
  }

  @PutMapping("/{username}")
  public ResponseEntity<Users> updateUser(@PathVariable String username, @RequestBody Users user) {
    Users updatedUser = userService.updateUser(username, user);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable String username) {
    userService.deleteUser(username);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<Users>> getAllUsers() {
    List<Users> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }
}
