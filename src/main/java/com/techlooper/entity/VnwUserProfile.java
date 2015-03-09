package com.techlooper.entity;

import org.dozer.Mapping;

/**
 * Created by phuonghqh on 1/19/15.
 */
public class VnwUserProfile extends UserProfile {

    @Mapping("emailAddress")
    private String email;

    @Mapping("firstName")
    private String firstname;

    @Mapping("lastName")
    private String lastname;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
