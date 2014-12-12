package com.techlooper.service.impl;

import com.techlooper.entity.UserEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.service.SocialService;
import org.springframework.social.connect.Connection;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

import static com.techlooper.model.SocialProvider.LINKEDIN;

/**
 * Created by phuonghqh on 12/11/14.
 */

@Service("LINKEDINService")
public class LinkedInServiceImpl implements SocialService {

  @Resource
  private LinkedInConnectionFactory linkedInConnectionFactory;

  private String redirectUri;

  @Resource
  private UserRepository userRepository;

  @Inject
  public LinkedInServiceImpl(JsonConfigRepository jsonConfigRepository) {
    redirectUri = jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> LINKEDIN == config.getProvider()).findFirst().get().getRedirectUri();
  }

  public AccessGrant getAccessGrant(String accessCode) {
//    LinkedInConnectionFactory factory = (LinkedInConnectionFactory) linkedInConnectionFactory;
    return linkedInConnectionFactory.getOAuthOperations().exchangeForAccess(accessCode, redirectUri, null);
//    Connection<LinkedIn> connection = factory.createConnection(accessGrant);
//    UserEntity userEntity = new UserEntity("7");
//    LinkedInProfileFull profile = connection.getApi().profileOperations().getUserProfileFull();
//    userEntity.setLinkedInProfileFull(profile);

//    userRepository.save(userEntity);
//    userRepository.findOne("7")
//    System.out.println(profile);
//    Connection<LinkedIn> connection = fac.createConnection(accessGrant);
//    connection.getKey();
//    AccessGrant accessGrant = factory.getOAuthOperations().exchangeForAccess(accessCode, redirectUri, null);
//    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(connectionKey.getProviderUserId(), null, null));
//    return accessGrant.getAccessToken();
  }

  public void persistProfile(AccessGrant accessGrant) {
    Connection<LinkedIn> connection = linkedInConnectionFactory.createConnection(accessGrant);
    LinkedInProfileFull profile = connection.getApi().profileOperations().getUserProfileFull();
    UserEntity.Builder builder = new UserEntity.Builder().withLoginSource(SocialProvider.LINKEDIN);
    builder.withFirstName(profile.getFirstName()).withLastName(profile.getLastName()).withEmailAddress(profile.getEmailAddress());
    userRepository.save(builder.build());
  }
}
