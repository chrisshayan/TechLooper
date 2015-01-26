package com.techlooper.entity;

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
@Document(indexName = "user_import", type = "user")
public class UserImportEntity {

  @Id
  private String email;

  @Field(type = String)
  private String fullName;

  @Field(type = FieldType.Nested)
  private Map<SocialProvider, Object> profiles = new HashMap<>();

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

  public static class UserImportEntityBuilder {

    private UserImportEntity userImportEntity;

    private UserImportEntityBuilder() {
    }

    public static UserImportEntityBuilder userImportEntity(UserImportEntity userImportEntity) {
      UserImportEntityBuilder builder = new UserImportEntityBuilder();
      builder.userImportEntity = userImportEntity;
      return builder;
    }

    public UserImportEntityBuilder withEmail(String email) {
      userImportEntity.email = email;
      return this;
    }

    public UserImportEntityBuilder withFullName(String fullName) {
      userImportEntity.fullName = fullName;
      return this;
    }

    public UserImportEntityBuilder withProfile(SocialProvider provider, Object profile) {
      userImportEntity.profiles.put(provider, profile);
      return this;
    }

    public UserImportEntity build() {
      return userImportEntity;
    }
  }
}
