package com.turbolessons.adminservice.controller;

import com.turbolessons.adminservice.dto.UserDTO;
import com.turbolessons.adminservice.dto.UserProfileDTO;
import com.turbolessons.adminservice.model.User;
import com.turbolessons.adminservice.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller for user management operations.
 * This demo version uses a modified UserService that doesn't depend on Okta.
 */
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.listAllUsers();
    }

    @GetMapping("/api/users/teacher/{username}")
    public List<User> getUsersByTeacher(@PathVariable String username) {
        return userService.listAllUsersByTeacher(username);
    }

    @GetMapping("/api/users/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping("/api/users/profile/{id}")
    public UserProfileDTO getUserProfile(@PathVariable String id) {
        return userService.getUserProfile(id);
    }

    @PostMapping("/api/users")
    public User createUser(@RequestBody UserDTO dto) {
        return userService.createUser(dto.getEmail(), dto.getFirstName(), dto.getLastName());
    }

    @PutMapping("/api/users/{id}")
    public void updateUser(@PathVariable String id, @RequestBody UserProfileDTO dto) {
        userService.updateUser(id, dto);
    }

    @DeleteMapping("/api/users/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
