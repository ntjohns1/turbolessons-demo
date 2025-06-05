package com.turbolessons.adminservice.service;

import com.turbolessons.adminservice.model.User;
import com.turbolessons.adminservice.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.turbolessons.adminservice.dto.UserProfileDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    // No need to mock APIs since we're using in-memory implementation
    @InjectMocks
    private UserService userService;
    private User mockedUser1;
    private User mockedUser2;

    @BeforeEach
    public void setUp() {
        // Create first user
        mockedUser1 = new User();
        mockedUser1.setId("user1");
        UserProfile profile1 = new UserProfile();
        profile1.setFirstName("Willie");
        profile1.setLastName("Nelson");
        profile1.setDisplayName("wnelson");
        profile1.setUserType("student");
        profile1.setEmail("willie@nelson.com");
        mockedUser1.setProfile(profile1);
        
        // Create second user
        mockedUser2 = new User();
        mockedUser2.setId("user2");
        UserProfile profile2 = new UserProfile();
        profile2.setEmail("janis@joplin.com");
        profile2.setFirstName("Janis");
        profile2.setLastName("Joplin");
        profile2.setDisplayName("jjoplin");
        profile2.setUserType("student");
        mockedUser2.setProfile(profile2);
    }

    @Test
    public void testListAllUsers() {
        // The UserService already has mock data initialized in its constructor
        List<User> users = userService.listAllUsers();
        // Just verify we get some users back
        assertTrue(users.size() > 0);
    }

    @Test
    public void testListAllUsersByTeacher() {
        // Test with a teacher that exists in the mock data
        List<User> users = userService.listAllUsersByTeacher("teacher1");
        // Verify we get the expected number of students for this teacher
        assertEquals(5, users.size());
    }

    @Test
    public void testGetUser() {
        // First create a user and add it to the service
        User newUser = userService.createUser("test@example.com", "Test", "User");
        String userId = newUser.getId();
        
        // Now retrieve the user and verify
        User retrievedUser = userService.getUser(userId);
        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getId());
        assertEquals("Test", retrievedUser.getProfile().getFirstName());
    }

    @Test
    public void testGetUserProfile() {
        // First create a user and add it to the service
        User newUser = userService.createUser("profile@example.com", "Profile", "Test");
        String userId = newUser.getId();
        
        // Now retrieve the user profile and verify
        UserProfileDTO profileDTO = userService.getUserProfile(userId);
        assertNotNull(profileDTO);
        assertEquals("Profile", profileDTO.getFirstName());
        assertEquals("Test", profileDTO.getLastName());
        assertEquals("profile@example.com", profileDTO.getEmail());
    }

    // createUser, updateUser, and deleteUser will be considered out of scope for unit testing
    // since they rely on direct interactions with the Okta server. See Integration tests
}

