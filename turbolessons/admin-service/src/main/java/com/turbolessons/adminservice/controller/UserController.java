package com.turbolessons.adminservice.controller;

import com.turbolessons.adminservice.dto.UserDTO;
import com.turbolessons.adminservice.dto.UserProfileDTO;
import com.turbolessons.adminservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openapitools.client.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Value("${okta.client.token}")
    private String apiToken;

    @Operation(summary = "Get all users", description = "Returns a list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        System.out.println(apiToken);
        return userService.listAllUsers();
    }

    @Operation(summary = "Get users by teacher", description = "Returns a list of users associated with a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @GetMapping("/api/users/teacher/{username}")
    public List<User> getUsersByTeacher(
            @Parameter(description = "Username of the teacher") @PathVariable String username) {
        System.out.println(apiToken);
        return userService.listAllUsersByTeacher(username);
    }

    @Operation(summary = "Get user by ID", description = "Returns a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/api/users/{id}")
    public User getUser(
            @Parameter(description = "ID of the user") @PathVariable String id) {
        return userService.getUser(id);
    }

    @Operation(summary = "Get user profile by ID", description = "Returns a user profile by user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @GetMapping("/api/users/profile/{id}")
    public UserProfileDTO getUserProfile(
            @Parameter(description = "ID of the user") @PathVariable String id) {
        return userService.getUserProfile(id);
    }

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created user"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/api/users")
    public User createUser(
            @Parameter(description = "User data") @RequestBody UserDTO dto) {
        return userService.createUser(dto.getEmail(), dto.getFirstName(), dto.getLastName());
    }

    @Operation(summary = "Update a user", description = "Updates an existing user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated user"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/api/users/{id}")
    public void updateUser(
            @Parameter(description = "ID of the user") @PathVariable String id, 
            @Parameter(description = "Updated user profile data") @RequestBody UserProfileDTO dto) {
        userService.updateUser(id, dto);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted user"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/api/users/{id}")
    public void deleteUser(
            @Parameter(description = "ID of the user") @PathVariable String id) {
        userService.deleteUser(id);
    }
}
