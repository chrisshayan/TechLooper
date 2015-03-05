package com.techlooper.entity.userimport;

import com.techlooper.model.SocialProvider;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by khoa-nd on 27/01/15.
 */
@Document(indexName = "techlooper", type = "user")
public class UserImportEntity {

    @Id
    private String email;

    @Field(type = String)
    private String fullName;

    @Field(type = FieldType.Nested)
    private Map<SocialProvider, Object> profiles = new HashMap<>();

    private boolean isCrawled = true;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<SocialProvider, Object> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<SocialProvider, Object> profiles) {
        this.profiles = profiles;
    }

    public boolean isCrawled() {
        return isCrawled;
    }

    public void setCrawled(boolean isCrawled) {
        this.isCrawled = isCrawled;
    }

    public void withProfile(SocialProvider provider, Object profile) {
        this.profiles.put(provider, profile);
    }
}
