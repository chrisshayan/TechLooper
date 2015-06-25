package com.techlooper.config;

import com.techlooper.repository.vnw.VnwUserRepo;
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
public class VmwDbConfigurationTest {

  @Resource
  private VnwUserRepo vnwUserRepo;

  @Test
  public void test() throws NoSuchAlgorithmException {
    String username = "";
    String password = "";
    String hashPassword = new String(MessageDigest.getInstance("MD5").digest(password.getBytes()));
  }
}
