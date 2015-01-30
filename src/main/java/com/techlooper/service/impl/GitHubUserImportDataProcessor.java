package com.techlooper.service.impl;

import com.techlooper.model.SocialProvider;
import com.techlooper.model.UserImportData;
import com.techlooper.service.UserImportDataProcessor;
import com.techlooper.util.EmailValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 1/30/15.
 */
@Service("GITHUBUserImportDataProcessor")
public class GitHubUserImportDataProcessor implements UserImportDataProcessor {

  public void process(List<UserImportData> users) {
    for(UserImportData user : users) {
      processUserEmail(user);
      extractUserSkillSetFromDescription(user);
      extractUserNumberOfRepositories(user);
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

  private void extractUserSkillSetFromDescription(UserImportData user) {
    if (user.getDescription().startsWith(user.getUsername())) {
      String[] tokens = StringUtils.split(user.getDescription(), " ");
      for (String token : tokens) {
        if (token.contains(EmailValidator.COMMA)) {
          user.getSkills().add(StringUtils.remove(token, EmailValidator.COMMA));
        } else if (token.contains(EmailValidator.DOT) && !token.toUpperCase().contains(SocialProvider.GITHUB.toString())) {
          user.getSkills().add(StringUtils.remove(token, EmailValidator.DOT));
        }
      }
    }
  }

  private void extractUserNumberOfRepositories(UserImportData user) {
    int beforeNumberRepoIndex = StringUtils.ordinalIndexOf(user.getDescription(), EmailValidator.WHITE_SPACE, 2);
    int afterNumberRepoIndex = StringUtils.ordinalIndexOf(user.getDescription(), EmailValidator.WHITE_SPACE, 3);
    String numRepoStr = StringUtils.substring(user.getDescription(), beforeNumberRepoIndex + 1, afterNumberRepoIndex);
    if (StringUtils.isNumeric(numRepoStr)) {
      user.setNumberOfRepositories(Integer.valueOf(numRepoStr));
    }
  }

  private void processUserFollowers(UserImportData user) {
    String followers = user.getFollowers();
    if (StringUtils.isNotEmpty(followers) && followers.contains("k")) {
      double numberOfFollowers = Double.valueOf(followers.replace("k", "")) * 1000;
      user.setFollowers(String.valueOf((int)numberOfFollowers));
    }
  }

  private void processUserFollowing(UserImportData user) {
    String following = user.getFollowing();
    if (StringUtils.isNotEmpty(following) && following.contains("k")) {
      double numberOfFollowing = Double.valueOf(following.replace("k", "")) * 1000;
      user.setFollowers(String.valueOf((int)numberOfFollowing));
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
