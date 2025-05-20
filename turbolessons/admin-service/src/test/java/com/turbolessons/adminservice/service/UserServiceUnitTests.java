package com.turbolessons.adminservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.api.GroupApi;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.Group;
import org.openapitools.client.model.User;
import org.openapitools.client.model.UserProfile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @Mock
    private UserApi userApi;
    @Mock
    private GroupApi groupApi;
    @InjectMocks
    private UserService userService;
    private User mockedUser1;
    private User mockedUser2;

    Group group;

    @BeforeEach
    public void setUp() {
        UserProfile profile = new UserProfile();
        // Mock the Group
        group = Mockito.mock(Group.class);

        // Stub the getId() method


        // Since assignUserToGroup is void, just mock it so that it doesn't do anything
        profile.setFirstName("Willie");
        profile.setLastName("Nelson");
        profile.setDisplayName("wnelson");
        profile.setUserType("student");
        mockedUser1 = new User();
        mockedUser1.setProfile(profile);
        profile.setEmail("janis@joplin.com");
        profile.setFirstName("Janis");
        profile.setLastName("Joplin");
        profile.setDisplayName("jjoplin.");
        profile.setUserType("student");
        mockedUser2 = new User();
        mockedUser2.setProfile(profile);
    }

    @Test
    public void testListAllUsers() {
        when(userApi.listUsers(null, null, 150, null, null, null, null))
                .thenReturn(List.of(mockedUser1,mockedUser2));
        List<User> users = userService.listAllUsers();
        assertEquals(2, users.size());
        assertEquals(mockedUser1, users.get(0));
    }

    @Test
    public void testListAllUsersByTeacher() {
        when(group.getId()).thenReturn("testId");
        when(groupApi.listGroups(any(),any(),any(),any(),any(),any())).thenReturn(List.of(group));
        when(groupApi.listGroupUsers(any(),any(),any())).thenReturn(List.of(mockedUser1));
        List<User> users = userService.listAllUsersByTeacher("test");
        assertEquals(1, users.size());
        assertEquals(mockedUser1, users.get(0));
    }

    @Test
    public void testGetUser() {
        when(userApi.getUser("testId")).thenReturn(mockedUser1);
        assertEquals(mockedUser1, userService.getUser("testId"));
    }

    @Test
    public void testGetUserProfile() {
        when(userApi.getUser("testId")).thenReturn(mockedUser1);
        String expected = Objects.requireNonNull(mockedUser1.getProfile()).getFirstName();
        String actual = userService.getUserProfile("testId").getFirstName();
        assertEquals(expected, actual);
    }

    // createUser, updateUser, and deleteUser will be considered out of scope for unit testing
    // since they rely on direct interactions with the Okta server. See Integration tests
}

