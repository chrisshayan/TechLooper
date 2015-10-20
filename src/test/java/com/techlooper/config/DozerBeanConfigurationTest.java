package com.techlooper.config;

import com.techlooper.converter.ListCSVStringConverter;
import com.techlooper.converter.LocaleConverter;
import com.techlooper.converter.ProfileNameConverter;
import com.techlooper.dto.WebinarInfoDto;
import com.techlooper.entity.*;
import com.techlooper.model.*;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.google.api.plus.Person;

/**
 * Created by phuonghqh on 10/20/15.
 */
@Configuration
public class DozerBeanConfigurationTest {

  @Bean
  public Mapper dozerBeanMapper() {
    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
    dozerBeanMapper.addMapping(new BeanMappingBuilder() {
      protected void configure() {
        mapping(FacebookProfile.class, com.techlooper.entity.FacebookProfile.class)
          .fields("locale", "locale", FieldsMappingOptions.customConverter(LocaleConverter.class));

        mapping(TwitterProfile.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
          .fields("name", "firstName", FieldsMappingOptions.copyByReference())
          .fields("screenName", "userName", FieldsMappingOptions.copyByReference());

        mapping(GitHubUserProfile.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
          .fields("name", "firstName", FieldsMappingOptions.copyByReference())
          .fields("email", "emailAddress", FieldsMappingOptions.copyByReference());

        mapping(Person.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
          .fields("givenName", "firstName", FieldsMappingOptions.copyByReference())
          .fields("familyName", "lastName", FieldsMappingOptions.copyByReference())
          .fields("accountEmail", "emailAddress", FieldsMappingOptions.copyByReference())
          .fields("imageUrl", "profileImageUrl", FieldsMappingOptions.copyByReference());

        mapping(com.techlooper.entity.FacebookProfile.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
          .fields("email", "emailAddress", FieldsMappingOptions.copyByReference());

        mapping(LinkedInProfile.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
          .fields("profilePictureUrl", "profileImageUrl", FieldsMappingOptions.copyByReference());

        mapping(UserEntity.class, UserInfo.class, TypeMappingOptions.oneWay())
          .fields("profiles", "profileNames", FieldsMappingOptions.customConverter(ProfileNameConverter.class));

        mapping(UserInfo.class, UserEntity.class, TypeMappingOptions.oneWay())
          .fields("profileNames", "profiles", FieldsMappingOptions.customConverter(ProfileNameConverter.class));

        mapping(UserEntity.class, VnwUserProfile.class).exclude("accessGrant");

        mapping(SalaryReviewEntity.class, VNWJobSearchRequest.class)
          .fields("jobLevelIds", "jobLevel")
          .fields("jobCategories", "jobCategories", FieldsMappingOptions.customConverter(ListCSVStringConverter.class))
          .fields("netSalary", "jobSalary")
          .fields("locationId", "jobLocation");

        mapping(VnwJobAlert.class, VnwJobAlertRequest.class)
          .fields("jobLocations", "locationId", FieldsMappingOptions.customConverter(ListCSVStringConverter.class))
          .fields("minSalary", "netSalary");

        mapping(WebinarInfoDto.class, WebinarEntity.class, TypeMappingOptions.oneWay())
          .exclude("createdDateTime");

        mapping(ScrapeJobEntity.class, VNWJobSearchResponseDataItem.class)
          .fields("jobTitle", "title")
          .fields("jobTitleUrl", "url")
          .fields("companyLogoUrl", "logoUrl")
          .fields("createdDateTime", "postedOn");

        mapping(ScrapeJobEntity.class, JobResponse.class)
          .fields("jobTitle", "title")
          .fields("jobTitleUrl", "url")
          .fields("createdDateTime", "postedOn")
          .fields("companyLogoUrl", "logoUrl");

        mapping(ChallengeEntity.class, ChallengeDto.class)
          .fields("startDateTime", "startDate")
          .fields("submissionDateTime", "submissionDate")
          .fields("registrationDateTime", "registrationDate")
          .fields("ideaSubmissionDateTime", "ideaSubmissionDate")
          .fields("uxSubmissionDateTime", "uxSubmissionDate")
          .fields("prototypeSubmissionDateTime", "prototypeSubmissionDate");

        mapping(ChallengeRegistrantDto.class, ChallengeRegistrantEntity.class, TypeMappingOptions.oneWay())
          .exclude("activePhase");

      }
    });
    return dozerBeanMapper;
  }
}
