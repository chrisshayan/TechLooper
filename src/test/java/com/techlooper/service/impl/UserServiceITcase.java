package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.UserEntity;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.*;
import com.techlooper.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchUserImportConfiguration.class})
public class UserServiceITcase {

    @Resource
    private UserService userService;

    @Test
    public void testSave() throws Exception {
        String key = "ndkhoa.is2@gmail.com";
        UserEntity user = new UserEntity();
        user.setId(key);
        user.setFirstName("Khoa");
        user.setLastName("Nguyen");
        user.setEmailAddress("ndkhm");
        user.setLoginSource(SocialProvider.LINKEDIN);

//        ProfileEntity profile = new ProfileEntity();
//        profile.setSns("LinkedIn");
//        profile.setId("zJROJ4-vlg");
//        profile.setLastName("Nguyen");
//        profile.setFirstName("Khoa");
//        profile.setAccessToken("b1ce044e-ca68-4fab-9590-f2708ffcd04c");
//        profile.setHeadline("Java Developer");
//        profile.setIndustry("Information Technology and Services");
//        profile.setLocation("vn");
//        profile.setNumConnections(119);
//        profile.setPictureUrl("profile.png");
//        profile.setPublicProfileUrl("https://www.linkedin.com/in/khoanguyendang");
//        Map<SocialProvider, ProfileEntity> profileEntityMap = new HashMap<>();
//        profileEntityMap.put(SocialProvider.LINKEDIN, profile);
//        user.setProfiles(profileEntityMap);

        userService.save(user);

        UserEntity found = userService.findById(key);
        assertEquals(found.getId(), user.getId());
    }

    @Test
    public void testFindById() throws Exception {
        UserEntity userEntity = userService.findById("ndkhoa.is2@gmail.com");
        assertNotNull(userEntity);
        assertEquals(userEntity.getFirstName(), "Khoa");
    }

    @Test
    public void testUserNotFound() throws Exception {
        UserEntity userEntity = userService.findById("id");
        assertNull(userEntity);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getAll() throws Exception {
        final List<UserImportEntity> all = userService.getAll(0, 10);
        assertNotNull(all);

        final FileWriter fileWriter = new FileWriter("test.csv");

        all.stream().forEach(user -> {
            final Map<String, Object> githubProfile = (Map<String, Object>) user.getProfiles().get(SocialProvider.GITHUB);
            if (githubProfile != null) {
                final List<String> skills = (List<String>) githubProfile.get("skills");
                System.out.println("githubProfile.get(\"skills\") = " + skills);
                assertNotNull(githubProfile);

                try {
                    fileWriter.append(user.getFullName());
                    fileWriter.append(",");
                    fileWriter.append(user.getEmail());
                    fileWriter.append(",");
                    if (CollectionUtils.isNotEmpty(skills)) {
                        fileWriter.append(skills.toString());
                    }
                    fileWriter.append("\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        fileWriter.flush();
        fileWriter.close();
    }

    @Test
    public void testFindTalent() throws Exception {
        TalentSearchRequest.Builder searchParam = new TalentSearchRequest.Builder();
        searchParam.withLocations(Arrays.asList("Ho Chi Minh", "Saigon", "HoChiMinh")).withPageSize(20).withPageIndex(0);
        TalentSearchResponse talents = userService.findTalent(searchParam.build());
        assertTrue(talents.getResult().size() > 0);
    }

    @Test
    public void testTalentNotFound() throws Exception {
        TalentSearchRequest.Builder searchParam = new TalentSearchRequest.Builder();
        searchParam.withSkills("English").withLocations("Vietnam")
                .withPageSize(20).withPageIndex(0);
        TalentSearchResponse talents = userService.findTalent(searchParam.build());
        assertTrue(talents.getResult().size() == 0);
    }

    @Test
    public void testGetTalentProfile() throws Exception {
        TalentProfile talentProfile = userService.getTalentProfile("falcon@falconsrv.net");
        assertTrue(talentProfile.getSkillMap().size() > 0);
        assertTrue(talentProfile.getUserImportEntity().getScore() > 0);
    }

    @Test
    public void testRegisterTalent() throws Exception {
        String email = "ndkhoa.is@gmail.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmailAddress(email);
        userInfo.setFirstName("Khoa");
        userInfo.setLastName("Nguyen");
        userService.registerUser(userInfo);

        UserEntity found = userService.findById(email);
        assertEquals(found.getId(), userInfo.getId());
    }
}