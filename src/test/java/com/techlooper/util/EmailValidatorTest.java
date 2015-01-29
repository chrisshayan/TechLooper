package com.techlooper.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmailValidatorTest {

  @Test
  public void testValidate() throws Exception {
    String[] emails = new String[] {"ndkhoa.is@gmail.com", "ndkhoa@gmail.com.vn", "ndkhoa.is@gmail.com.vn"};
    for (String email : emails) {
      assertTrue(EmailValidator.validate(email));
    }
  }
}