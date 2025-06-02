package com.turbolessons.adminservice.model;

/**
 * User model for demo purposes.
 * This replaces the Okta User model to avoid dependencies on external services.
 */
public class User {
    private String id;
    private UserProfile profile = new UserProfile();
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public UserProfile getProfile() {
        return profile;
    }
    
    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
