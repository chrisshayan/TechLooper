package com.techlooper.service.impl;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.LinkedInProfile;
import com.techlooper.entity.UserProfile;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

import static com.techlooper.model.SocialProvider.LINKEDIN;

/**
 * Created by phuonghqh on 12/11/14.
 */

@Service("LINKEDINService")
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
public class LinkedInService extends AbstractSocialService {

    @Resource
    private LinkedInConnectionFactory linkedInConnectionFactory;

    @Inject
    public LinkedInService(JsonConfigRepository jsonConfigRepository) {
        super(jsonConfigRepository, LINKEDIN);
    }

    public OAuth2ConnectionFactory getOAuth2ConnectionFactory() {
        return linkedInConnectionFactory;
    }

    public UserProfile getProfile(AccessGrant accessGrant) {
        Connection<LinkedIn> connection = linkedInConnectionFactory.createConnection(getAccessGrant(accessGrant));
        LinkedInProfileFull profile = connection.getApi().profileOperations().getUserProfileFull();
        LinkedInProfile liProfile = dozerBeanMapper.map(profile, LinkedInProfile.class);
        liProfile.setAccessGrant(accessGrant);
        return liProfile;
    }
}
