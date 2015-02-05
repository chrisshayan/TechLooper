package com.techlooper.service.impl;

import com.techlooper.model.UserImportData;
import com.techlooper.service.UserImportDataProcessor;
import com.techlooper.util.EmailValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NguyenDangKhoa on 1/30/15.
 */
@Service("GITHUBUserImportDataProcessor")
public class GitHubUserImportDataProcessor implements UserImportDataProcessor {

  public void process(List<UserImportData> users) {
    for (UserImportData user : users) {
      processUserEmail(user);
      extractUserSkillSetFromDescription(user);
      processUserFollowers(user);
      processUserFollowing(user);
      processUserContributedLongStreakDays(user);
      processUserContributedNumberLastYear(user);
    }
  }

  private void processUserEmail(UserImportData user) {
    if (StringUtils.isEmpty(user.getEmail()) || !EmailValidator.validate(user.getEmail())) {
      user.setOriginalEmail(user.getEmail());
      user.setEmail(user.getUsername() + "@missing.com");
    }
  }

  private static void extractUserSkillSetFromDescription(UserImportData user) {
    if (StringUtils.isNotEmpty(user.getDescription())) {
      final String USER_DESCRIPTION_PATTERN = "([A-Za-z0-9-_]+)*\\shas\\s([0-9]+)*\\s(repositories|repository)\\swritten\\sin\\s"
              + "(([-\\w\\s,\\+#\\(\\)']+)*|([-\\w\\s,\\+#\\(\\)']+)*\\sand\\s([-\\w\\+#\\(\\)']+)*)\\.\\s"
              + "Follow\\stheir\\scode\\son\\sGitHub\\.";
      Pattern pattern = Pattern.compile(USER_DESCRIPTION_PATTERN);
      Matcher matcher = pattern.matcher(user.getDescription());
      if (matcher.matches()) {
        user.setNumberOfRepositories(Integer.valueOf(matcher.group(2)));
        String skills = matcher.group(4).replaceAll("and", EmailValidator.COMMA);
        user.setSkills(Arrays.asList(StringUtils.split(StringUtils.deleteWhitespace(skills), EmailValidator.COMMA)));
      }
    }
  }

  private void processUserFollowers(UserImportData user) {
    String followers = user.getFollowers();
    if (StringUtils.isNotEmpty(followers) && followers.contains("k")) {
      double numberOfFollowers = Double.valueOf(followers.replace("k", "")) * 1000;
      user.setFollowers(String.valueOf((int) numberOfFollowers));
    }
  }

  private void processUserFollowing(UserImportData user) {
    String following = user.getFollowing();
    if (StringUtils.isNotEmpty(following) && following.contains("k")) {
      double numberOfFollowing = Double.valueOf(following.replace("k", "")) * 1000;
      user.setFollowers(String.valueOf((int) numberOfFollowing));
    }
  }

  private void processUserContributedLongStreakDays(UserImportData user) {
    user.setContributedLongestStreakTotal(StringUtils.isNotEmpty(user.getContributedLongestStreakTotal()) ?
            user.getContributedLongestStreakTotal().replace(" days", "") : "");
    user.setContributeCurrentStreakTotal(StringUtils.isNotEmpty(user.getContributeCurrentStreakTotal()) ?
            user.getContributeCurrentStreakTotal().replace(" days", "") : "");
  }

  private void processUserContributedNumberLastYear(UserImportData user) {
    user.setContributeNumberLastYear(StringUtils.isNotEmpty(user.getContributeNumberLastYear()) ?
            user.getContributeNumberLastYear().replace(" total", "") : "");
  }

}
