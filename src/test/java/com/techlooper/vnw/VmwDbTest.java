package com.techlooper.vnw;

import com.techlooper.config.BaseConfigurationTest;
import com.techlooper.config.VmwDbConfiguration;
import com.techlooper.repository.vnw.VnwUserRepo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by phuonghqh on 6/25/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BaseConfigurationTest.class, VmwDbConfiguration.class})
public class VmwDbTest {

  @Resource
  private VnwUserRepo vnwUserRepo;

  @Test
  public void testCheckVnwUser() throws NoSuchAlgorithmException {
    String username = "phuonghqh@yopmail.com";
    String password = "1234567890";
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.digest();
    String hashPassword = new String(md5.digest(password.getBytes()));
    Assert.assertNotNull(vnwUserRepo.findByUsernameIgnoreCaseAndUserPass(username, hashPassword));
  }
}
