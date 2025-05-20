package com.turbolessons.adminservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turbolessons.adminservice.dto.UserProfileDTO;
import com.okta.sdk.resource.user.UserBuilder;
import org.openapitools.client.api.GroupApi;
import org.openapitools.client.api.UserApi;
import org.openapitools.client.model.Group;
import org.openapitools.client.model.UpdateUserRequest;
import org.openapitools.client.model.User;
import org.openapitools.client.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserApi userApi;
    private final GroupApi groupApi;

    @Autowired
    private CacheManager cacheManager;

    public UserService(UserApi userApi, GroupApi groupApi) {
        this.userApi = userApi;
        this.groupApi = groupApi;
    }

    //    Get All Users
    @Cacheable(value = "userCache", key = "'listAllUsers'")
    public List<User> listAllUsers() {
        //        users.removeIf(u -> !Objects.equals(Objects.requireNonNull(u.getProfile()).getUserType(), "student"));
        return userApi.listUsers(
                null,
                null,
                150,
                null,
                null,
                null,
                null);
    }

    @Cacheable(value = "userCache", key = "'listAllUsersByTeacher:' + #teacherUsername")
    public List<User> listAllUsersByTeacher(String teacherUsername) {
        //        users.removeIf(u -> !Objects.equals(Objects.requireNonNull(u.getProfile()).getUserType(), "student"));
        String groupName = "active_student_" + teacherUsername;
        List<Group> g = groupApi.listGroups(groupName, null,null,1,null,null);

        return groupApi.listGroupUsers(g.get(0).getId(),null,150);
    }



    @Cacheable(value = "userCache", key = "#id", unless = "#result == null")
    public User getUser(String id) {
        Optional<User> u = Optional.ofNullable(userApi.getUser(id));
        return u.orElse(null);
    }

    public UserProfileDTO getUserProfile(String id) {
        UserProfileDTO dto = new UserProfileDTO();
        UserProfile userProfile = userApi.getUser(id).getProfile();
        assert userProfile != null;
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

    @Caching(put = {
            @CachePut(value = "userCache", key = "#result.id"),
    }, evict = {
            @CacheEvict(value = "userCache", key = "'listAllUsers'"),
            @CacheEvict(value = "userCache", key = "'listAllUsersByTeacher:' + #teacherUsername")
    })
    public User createUser(String email, String firstName, String lastName) {
        User user = userBuilder()
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setGroups(List.of("00g75cbo2dVoDm1wv5d7"))
                .buildAndCreate(userApi);
        String firstInitial = String.valueOf(firstName.charAt(0));
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UserProfile profile = new UserProfile();
        // Apply new profile object to request object
        profile.setDisplayName(firstInitial + lastName);
        profile.setUserType("student");
        updateUserRequest.setProfile(profile);
        // then update
        userApi.updateUser(user.getId(), updateUserRequest, true);
        return user;
    }

    @Caching(put = {
            @CachePut(value = "userCache", key = "#userId")
    }, evict = {
            @CacheEvict(value = "userCache", key = "'listAllUsers'"),
            @CacheEvict(value = "userCache", key = "'listAllUsersByTeacher:' + #teacherUsername")
    })
    public void updateUser(String userId, UserProfileDTO userProfileDTO) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(userProfileDTO, new TypeReference<>() {
        });
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        UserProfile userProfile = new UserProfile();
        userProfile.setAdditionalProperties(map);
        updateUserRequest.setProfile(userProfile);
        userApi.updateUser(userId, updateUserRequest, true);
    }

    @Caching(evict = {
            @CacheEvict(value = "userCache", key = "#id"),
            @CacheEvict(value = "userCache", key = "'listAllUsers'"),
            @CacheEvict(value = "userCache", key = "'listAllUsersByTeacher:' + #teacherUsername")
    })
    public void deleteUser(String id) {
        // deactivate first
        userApi.deactivateUser(id, false);
        // then delete
        userApi.deleteUser(id, false);
    }

    // This method wraps UserBuilder.instance()
    protected UserBuilder userBuilder() {
        return UserBuilder.instance();
    }
}

