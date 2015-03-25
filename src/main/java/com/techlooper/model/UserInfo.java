package com.techlooper.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Created by phuonghqh on 12/12/14.
 */
public class UserInfo {

    private String id;

    @NotNull
    @Email
    private String emailAddress;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String createdDateTime;

    private SocialProvider loginSource;

    private String key;

    private String username;

    private Set<SocialProvider> profileNames;

    private Integer salary;

    private String profileImageUrl;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Set<SocialProvider> getProfileNames() {
        return profileNames;
    }

    public void setProfileNames(Set<SocialProvider> profileNames) {
        this.profileNames = profileNames;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SocialProvider getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(SocialProvider loginSource) {
        this.loginSource = loginSource;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean hasProfile(SocialProvider socialProvider) {
        return profileNames.contains(socialProvider);
    }

    public boolean removeProfile(SocialProvider socialProvider) {
        return profileNames.remove(socialProvider);
    }

    public boolean acceptRegisterVietnamworksAccount() {
        return hasProfile(SocialProvider.VIETNAMWORKS);
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
