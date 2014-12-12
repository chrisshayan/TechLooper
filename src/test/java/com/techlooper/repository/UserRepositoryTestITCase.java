package com.techlooper.repository;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.CouchbaseConfiguration;
import com.techlooper.entity.ProfileEntity;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.couchbase.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, CouchbaseConfiguration.class})
public class UserRepositoryTestITCase {

    @Resource
    private UserRepository userRepository;

    @Test
    public void testSave() throws Exception {
        String key = "ndkhoa.is2@gmail.com";
        UserEntity user = new UserEntity();
        user.setId(key);
        user.setFirstName("Khoa");
        user.setLastName("Nguyen");
        user.setEmailAddress("ndkhm");
        user.setLoginSource(SocialProvider.LINKEDIN);

        ProfileEntity profile = new ProfileEntity();
        profile.setSns("LinkedIn");
        profile.setId("zJROJ4-vlg");
        profile.setLastName("Nguyen");
        profile.setFirstName("Khoa");
        profile.setAccessToken("b1ce044e-ca68-4fab-9590-f2708ffcd04c");
        profile.setHeadline("Java Developer");
        profile.setIndustry("Information Technology and Services");
        profile.setLocation("vn");
        profile.setNumConnections(119);
        profile.setPictureUrl("profile.png");
        profile.setPublicProfileUrl("https://www.linkedin.com/in/khoanguyendang");
        Map<SocialProvider, ProfileEntity> profileEntityMap = new HashMap<>();
        profileEntityMap.put(SocialProvider.LINKEDIN, profile);
        user.setProfiles(profileEntityMap);

        userRepository.save(user);

        UserEntity found = userRepository.findOne(key);
        assertEquals(found.getId(), user.getId());
    }

    @Test
    public void testFindById() throws Exception {
        UserEntity userEntity = userRepository.findOne("ndkhoa.is2@gmail.com");
        assertNotNull(userEntity);
        assertEquals(userEntity.getFirstName(), "Khoa");
    }

}
