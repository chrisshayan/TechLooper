package com.techlooper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NguyenDangKhoa on 1/29/15.
 */
public class EmailValidator {

  private static final String EMAIL_PATTERN =
          "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                  + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

  private static Pattern pattern = Pattern.compile(EMAIL_PATTERN);
  private static Matcher matcher;

  public static boolean validate(final String email) {
    matcher = pattern.matcher(email);
    return matcher.matches();
  }

}
