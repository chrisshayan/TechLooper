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

    @Field
    private Double rate;

    @Field
    private Map<String, Integer> ranks;

    @Field
    private Double score;

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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setIsCrawled(boolean isCrawled) {
        this.isCrawled = isCrawled;
    }

    public void withProfile(SocialProvider provider, Object profile) {
        this.profiles.put(provider, profile);
    }

    public Map<java.lang.String, Integer> getRanks() {
        return ranks;
    }

    public void setRanks(Map<java.lang.String, Integer> ranks) {
        this.ranks = ranks;
    }
}
