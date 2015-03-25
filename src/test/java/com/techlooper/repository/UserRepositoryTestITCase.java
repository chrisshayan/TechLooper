package com.techlooper.repository;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.CouchbaseConfiguration;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.couchbase.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, CouchbaseConfiguration.class, ElasticsearchUserImportConfiguration.class})
public class UserRepositoryTestITCase {

    @Resource
    private UserRepository userRepository;

    @Test
    public void testSave() throws Exception {
        String key = "ndkhoa.fat@gmail.com";
        UserEntity user = new UserEntity();
        user.setId(key);
        user.setFirstName("Khoa");
        user.setLastName("Nguyen");
        user.setEmailAddress("ndkhoa.fat@gmail.com");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        user.setCreatedDateTime(sdf.format(new Date()));
        userRepository.save(user);

        UserEntity found = userRepository.findOne(key);
        assertEquals(found.getId(), user.getId());
    }

    @Test
    public void testFindById() throws Exception {
        UserEntity userEntity = userRepository.findOne("ndkhoa.is@gmail.com");
        assertNotNull(userEntity);
        assertEquals(userEntity.getFirstName(), "Khoa");
    }

}
