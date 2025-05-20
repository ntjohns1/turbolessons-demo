package com.turbolessons.adminservice.controller;

import com.turbolessons.adminservice.dto.UserDTO;
import com.turbolessons.adminservice.dto.UserProfileDTO;
import com.turbolessons.adminservice.service.UserService;
import org.openapitools.client.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Value("${okta.client.token}")
    private String apiToken;

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        System.out.println(apiToken);
        return userService.listAllUsers();
    }

    @GetMapping("/api/users/teacher/{username}")
    public List<User> getUsersByTeacher(@PathVariable String username) {
        System.out.println(apiToken);
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
