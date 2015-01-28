package com.techlooper.repository;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.entity.GitHubUserProfile;
import com.techlooper.entity.UserImportEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.elasticsearch.UserImportRepository;
import com.techlooper.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by khoa-nd on 27/01/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class})
public class UserImportRepositoryITCase {

  @Resource
  private UserImportRepository userImportRepository;

  @Resource
  private UserService userService;

  @Before
  public void empty() {
    assertNotNull(userImportRepository);
  }

  @Test
  public void testAddUser() {
    UserImportEntity userImportEntity = new UserImportEntity();
    userImportEntity.setEmail("ndkhoa.is@gmail.com");
    userImportEntity.setFullName("Khoa Nguyen");
    userImportEntity.setCrawled(true);
    GitHubUserProfile gitHubUserProfile = new GitHubUserProfile();
    gitHubUserProfile.setLocation("Vietnam");
    gitHubUserProfile.setUsername("khoa-nd");
    userImportEntity.withProfile(SocialProvider.GITHUB, gitHubUserProfile);

    assertNotNull(userImportRepository.save(userImportEntity));
  }

  @Test
  @DependsOn("testAddUser")
  public void testUserExist() {
    assertTrue(userImportRepository.exists("ndkhoa.is@gmail.com"));
  }

  @Test
  @DependsOn("testAddUser")
  public void testUserProfiles() {
    UserImportEntity userImportEntity = userImportRepository.findOne("ndkhoa.is@gmail.com");
    assertNotNull(userImportEntity.getProfiles().get(SocialProvider.GITHUB));
  }

  @Test
  @DependsOn("testAddUser")
  public void testSearchUserImportByEmail() {
    UserImportEntity userImportEntity = userService.findUserImportByEmail("ndkhoa.is@gmail.com");
    assertNotNull(userImportEntity);
  }

}
