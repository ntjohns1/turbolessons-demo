package com.turbolessons.adminservice.service;

import com.turbolessons.adminservice.dto.UserProfileDTO;
import com.turbolessons.adminservice.model.User;
import com.turbolessons.adminservice.model.UserProfile;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Mock implementation of UserService for demo purposes.
 * This service provides mock user data instead of connecting to Okta.
 */
@Service
public class UserService {

    // In-memory storage for users
    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    private final Map<String, List<String>> teacherToStudentsMap = new ConcurrentHashMap<>();
    
    public UserService() {
        // Initialize with mock data
        initializeMockData();
    }

    /**
     * Initialize mock data for demo purposes
     */
    private void initializeMockData() {
        // Create some mock teachers
        User teacher1 = createMockUser("teacher1@example.com", "John", "Smith", "teacher");
        User teacher2 = createMockUser("teacher2@example.com", "Jane", "Doe", "teacher");
        
        // Create some mock students for each teacher
        List<String> teacher1Students = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User student = createMockUser(
                "student" + i + "@example.com",
                "Student" + i,
                "Lastname" + i,
                "student"
            );
            teacher1Students.add(student.getId());
        }
        
        List<String> teacher2Students = new ArrayList<>();
        for (int i = 6; i <= 10; i++) {
            User student = createMockUser(
                "student" + i + "@example.com",
                "Student" + i,
                "Lastname" + i,
                "student"
            );
            teacher2Students.add(student.getId());
        }
        
        // Map teachers to their students
        teacherToStudentsMap.put("teacher1", teacher1Students);
        teacherToStudentsMap.put("teacher2", teacher2Students);
    }
    
    /**
     * Create a mock user with the given information
     */
    private User createMockUser(String email, String firstName, String lastName, String userType) {
        User user = new User();
        UserProfile profile = new UserProfile();
        
        user.setId(UUID.randomUUID().toString());
        profile.setEmail(email);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setUserType(userType);
        profile.setLogin(email);
        profile.setDisplayName(firstName + " " + lastName);
        
        // Add some mock address data
        profile.setStreetAddress("123 Main St");
        profile.setCity("Anytown");
        profile.setState("CA");
        profile.setZipCode("12345");
        profile.setMobilePhone("555-123-4567");
        
        user.setProfile(profile);
        userStore.put(user.getId(), user);
        
        return user;
    }

    /**
     * Get all users in the system
     */
    @Cacheable(value = "userCache", key = "'listAllUsers'")
    public List<User> listAllUsers() {
        return new ArrayList<>(userStore.values());
    }

    /**
     * Get all users associated with a specific teacher
     */
    @Cacheable(value = "userCache", key = "'listAllUsersByTeacher:' + #teacherUsername")
    public List<User> listAllUsersByTeacher(String teacherUsername) {
        List<String> studentIds = teacherToStudentsMap.getOrDefault(teacherUsername, Collections.emptyList());
        return studentIds.stream()
                .map(userStore::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }



    /**
     * Get a specific user by ID
     */
    @Cacheable(value = "userCache", key = "#id", unless = "#result == null")
    public User getUser(String id) {
        return userStore.get(id);
    }

    /**
     * Get a user's profile information
     */
    public UserProfileDTO getUserProfile(String id) {
        User user = userStore.get(id);
        if (user == null) {
            return null;
        }
        
        UserProfile userProfile = user.getProfile();
        UserProfileDTO dto = new UserProfileDTO();
        
        dto.setLogin(userProfile.getLogin());
        dto.setDisplayName(userProfile.getDisplayName());
        dto.setFirstName(userProfile.getFirstName());
        dto.setMiddleName(userProfile.getMiddleName());
        dto.setLastName(userProfile.getLastName());
        dto.setEmail(userProfile.getEmail());
        dto.setMobilePhone(userProfile.getMobilePhone());
        dto.setPrimaryPhone(userProfile.getPrimaryPhone());
        dto.setStreetAddress(userProfile.getStreetAddress());
        dto.setCity(userProfile.getCity());
        dto.setState(userProfile.getState());
        dto.setZipCode(userProfile.getZipCode());
        dto.setUserType(userProfile.getUserType());
        
        return dto;
    }

    /**
     * Create a new user
     */
    @Caching(put = {
            @CachePut(value = "userCache", key = "#result.id"),
    }, evict = {
            @CacheEvict(value = "userCache", key = "'listAllUsers'")
    })
    public User createUser(String email, String firstName, String lastName) {
        return createMockUser(email, firstName, lastName, "student");
    }

    /**
     * Update an existing user's profile
     */
    @Caching(evict = {
            @CacheEvict(value = "userCache", key = "#userId"),
            @CacheEvict(value = "userCache", key = "'listAllUsers'")
    })
    public void updateUser(String userId, UserProfileDTO userProfileDTO) {
        User user = userStore.get(userId);
        if (user != null) {
            UserProfile profile = user.getProfile();
            
            if (userProfileDTO.getLogin() != null) profile.setLogin(userProfileDTO.getLogin());
            if (userProfileDTO.getDisplayName() != null) profile.setDisplayName(userProfileDTO.getDisplayName());
            if (userProfileDTO.getFirstName() != null) profile.setFirstName(userProfileDTO.getFirstName());
            if (userProfileDTO.getMiddleName() != null) profile.setMiddleName(userProfileDTO.getMiddleName());
            if (userProfileDTO.getLastName() != null) profile.setLastName(userProfileDTO.getLastName());
            if (userProfileDTO.getEmail() != null) profile.setEmail(userProfileDTO.getEmail());
            if (userProfileDTO.getMobilePhone() != null) profile.setMobilePhone(userProfileDTO.getMobilePhone());
            if (userProfileDTO.getPrimaryPhone() != null) profile.setPrimaryPhone(userProfileDTO.getPrimaryPhone());
            if (userProfileDTO.getStreetAddress() != null) profile.setStreetAddress(userProfileDTO.getStreetAddress());
            if (userProfileDTO.getCity() != null) profile.setCity(userProfileDTO.getCity());
            if (userProfileDTO.getState() != null) profile.setState(userProfileDTO.getState());
            if (userProfileDTO.getZipCode() != null) profile.setZipCode(userProfileDTO.getZipCode());
            if (userProfileDTO.getUserType() != null) profile.setUserType(userProfileDTO.getUserType());
        }
    }

    /**
     * Delete a user
     */
    @Caching(evict = {
            @CacheEvict(value = "userCache", key = "#id"),
            @CacheEvict(value = "userCache", key = "'listAllUsers'")
    })
    public void deleteUser(String id) {
        userStore.remove(id);
        
        // Remove from teacher-student mappings if needed
        for (Map.Entry<String, List<String>> entry : teacherToStudentsMap.entrySet()) {
            entry.getValue().remove(id);
        }
    }


}

