package com.techlooper.entity;

import com.techlooper.entity.ProfileEntity;
import com.techlooper.model.SocialProvider;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.List;
import java.util.Map;

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
    private SocialProvider loginSource;

    @Field
    private Map<SocialProvider, ProfileEntity> profiles;

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


    public SocialProvider getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(SocialProvider loginSource) {
        this.loginSource = loginSource;
    }

    public Map<SocialProvider, ProfileEntity> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<SocialProvider, ProfileEntity> profiles) {
        this.profiles = profiles;
    }
}
