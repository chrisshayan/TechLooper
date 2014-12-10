package com.techlooper.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 12/10/14.
 */
@Document
public class UserEntity {

    @Id
    private String id;

    @Field
    private String emailAddress;

    @Field
    private String firstName;

    @Field
    private String lastName;

    @Field
    private String password;

    @Field
    private List<String> loginSources;

    @Field
    private List<ProfileEntity> profiles;

    public UserEntity(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getLoginSources() {
        return loginSources;
    }

    public void setLoginSources(List<String> loginSources) {
        this.loginSources = loginSources;
    }

    public List<ProfileEntity> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ProfileEntity> profiles) {
        this.profiles = profiles;
    }
}
