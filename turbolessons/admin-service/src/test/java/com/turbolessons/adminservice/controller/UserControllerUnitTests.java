package com.turbolessons.adminservice.controller;

import com.turbolessons.adminservice.dto.UserProfileDTO;
import com.turbolessons.adminservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openapitools.client.model.User;
import org.openapitools.client.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    private User mockedUser;

    @BeforeEach
    public void setUp() {
        UserProfile profile = new UserProfile();
        profile.setEmail("willie@nelson.com");
        profile.setFirstName("Willie");
        profile.setLastName("Nelson");
        profile.setDisplayName("Willie N.");
        profile.setUserType("student");
        mockedUser = new User();
        mockedUser.setProfile(profile);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.listAllUsers()).thenReturn(Collections.singletonList(mockedUser));
        mockMvc.perform(get("/api/users").with(jwt())).andExpect(status().isOk()).andExpect(jsonPath("$[0].profile.displayName").value(Objects.requireNonNull(mockedUser.getProfile()).getDisplayName()));
    }

    @Test
    public void testGetUsersByTeacher() throws Exception {
        when(userService.listAllUsersByTeacher(any())).thenReturn(Collections.singletonList(mockedUser));
        mockMvc.perform(get("/api/users/teacher/test").with(jwt())).andExpect(status().isOk()).andExpect(jsonPath("$[0].profile.displayName").value(Objects.requireNonNull(mockedUser.getProfile()).getDisplayName()));
    }

    @Test
    public void testGetUser() throws Exception {
        when(userService.getUser("testId")).thenReturn(mockedUser);
        mockMvc.perform(get("/api/users/testId").with(jwt())).andExpect(status().isOk()).andExpect(jsonPath("$.profile.displayName").value(Objects.requireNonNull(mockedUser.getProfile()).getDisplayName()));
    }

    @Test
    public void testGetUserProfile() throws Exception {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setDisplayName("Willie N.");
        when(userService.getUserProfile("testId")).thenReturn(dto);
        mockMvc.perform(get("/api/users/profile/testId").with(jwt())).andExpect(status().isOk()).andExpect(jsonPath("$.displayName").value(Objects.requireNonNull(dto.getDisplayName())));
    }

    @Test
    public void testCreateUser() throws Exception {

        when(userService.createUser(any(), any(), any())).thenReturn(mockedUser);
        mockMvc.perform(post("/api/users").with(jwt()).contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"test@test.com\",\"firstName\":\"Nelson\",\"lastName\":\"Willie\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.id").hasJsonPath());
    }

    @Test
    public void testUpdateUser() throws Exception {

        UserProfileDTO expectedProfile = new UserProfileDTO();
        expectedProfile.setEmail("willie@nelson.com");
        expectedProfile.setFirstName("Nelson");
        expectedProfile.setLastName("William");

        mockMvc.perform(put("/api/users/testUser").with(jwt()).contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"willie@nelson.com\",\"firstName\":\"Nelson\",\"lastName\":\"William\"}")).andExpect(status().isOk());

        verify(userService).updateUser(eq("testUser"), argThat(profile -> {
            return profile.getEmail().equals(expectedProfile.getEmail()) && profile.getFirstName().equals(expectedProfile.getFirstName()) && profile.getLastName().equals(expectedProfile.getLastName());
        }));
    }
}
