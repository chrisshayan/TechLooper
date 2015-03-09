package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.GitHubFollower;
import com.techlooper.entity.GitHubRepo;
import com.techlooper.entity.UserProfile;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static com.techlooper.model.SocialProvider.GITHUB;

/**
 * Created by phuonghqh on 12/16/14.
 */
@Service("GITHUBService")
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
public class GitHubService extends AbstractSocialService {

    @Resource
    private GitHubConnectionFactory gitHubConnectionFactory;

    @Inject
    public GitHubService(JsonConfigRepository jsonConfigRepository) {
        super(jsonConfigRepository, GITHUB);
    }

    public OAuth2ConnectionFactory getOAuth2ConnectionFactory() {
        return gitHubConnectionFactory;
    }

    public UserProfile getProfile(AccessGrant accessGrant) {
        Connection<GitHub> connection = gitHubConnectionFactory.createConnection(getAccessGrant(accessGrant));
        com.techlooper.entity.GitHubUserProfile profile = dozerBeanMapper.map(connection.getApi().userOperations().getUserProfile(), com.techlooper.entity.GitHubUserProfile.class);
        String profileImageUrl = connection.getApi().restOperations()
                .getForObject(socialConfig.getApiUrl().get("users"), JsonNode.class, profile.getUsername()).get("avatar_url").asText();
        profile.setProfileImageUrl(profileImageUrl);
        buildProfile(connection, profile);
        profile.setAccessGrant(accessGrant);
        return profile;
    }

    private void buildProfile(Connection<GitHub> connection, com.techlooper.entity.GitHubUserProfile profile) {
        String username = profile.getUsername();
        profile.setRepos(getForUserRepos(connection, username));
        profile.setFollowers(getForUserFollowers(connection, username));
    }

    private List<GitHubFollower> getForUserFollowers(Connection<GitHub> connection, String username) {
        return Arrays.asList(connection.getApi().restOperations()
                .getForObject(socialConfig.getApiUrl().get("followers"), GitHubFollower[].class, username));
    }

    private List<GitHubRepo> getForUserRepos(Connection<GitHub> connection, String username) {
        return Arrays.asList(connection.getApi().restOperations()
                .getForObject(socialConfig.getApiUrl().get("repos"), GitHubRepo[].class, username));
    }
}
