package org.example.rest_api_maven.controller;

import org.example.rest_api_maven.model.User;
import org.example.rest_api_maven.repository.UserRepository;
import org.example.rest_api_maven.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/checkNim/{nim}")
    public ResponseEntity<Object> checkNim(@PathVariable Long nim) {
        User user = userRepository.findByNim(nim);
        if (user != null) {
            // User with the provided NIM exists
            return ResponseEntity.ok(user);
        } else {
            // User not found with the provided NIM
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with NIM: " + nim);
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
