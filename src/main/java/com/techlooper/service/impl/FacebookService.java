package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.UserProfile;
import com.techlooper.model.SocialConfig;
import com.techlooper.repository.JsonConfigRepository;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static com.techlooper.model.SocialProvider.FACEBOOK;

/**
 * Created by phuonghqh on 12/15/14.
 */
@Service("FACEBOOKService")
public class FacebookService extends AbstractSocialService {

  @Resource
  private FacebookConnectionFactory facebookConnectionFactory;

  @Inject
  public FacebookService(JsonConfigRepository jsonConfigRepository) {
    super(jsonConfigRepository, FACEBOOK);
  }

  public OAuth2ConnectionFactory getOAuth2ConnectionFactory() {
    return facebookConnectionFactory;
  }

  public UserProfile getProfile(AccessGrant accessGrant) {
    Connection<Facebook> connection = facebookConnectionFactory.createConnection(getAccessGrant(accessGrant));
    FacebookProfile profile = connection.getApi().userOperations().getUserProfile();
    com.techlooper.entity.FacebookProfile fbProfile = dozerBeanMapper.map(profile, com.techlooper.entity.FacebookProfile.class);
    String profileImageUrl = connection.getApi().restOperations().getForObject(socialConfig.getApiUrl().get("picture"), JsonNode.class).at("/data/url").asText();
    fbProfile.setProfileImageUrl(profileImageUrl);
    fbProfile.setAccessGrant(accessGrant);
    return fbProfile;
  }

  public String getUserEmail(String code, SocialConfig socialConfig) {
    org.springframework.social.oauth2.AccessGrant access = facebookConnectionFactory.getOAuthOperations().exchangeForAccess(code, socialConfig.getRedirectUri(), null);
    org.springframework.social.connect.UserProfile userProfile = facebookConnectionFactory.createConnection(access).fetchUserProfile();
    return userProfile.getEmail();
  }

//  public static void main(String[] args) {
//    FacebookConnectionFactory abc = new FacebookConnectionFactory("775424035845076", "48114f64ce77e617b7f83fc055456217");
//    org.springframework.social.oauth2.AccessGrant def = abc.getOAuthOperations().exchangeForAccess("AQBuzEUqdoNOzopAYuGsdo78SIN4pzn5vblLinAubG3l-CFRs9FdqM42lVlH2xTalttxXclpSElXtIPKhNMWkhNWR-TXlPWrbzUC3TL72vecclCgdag71kzBAiUzFnlmj_XOndxRcLn_7P0OVcGLNoxS-_o9D3-_1yB_KEVfm3IBvsIbPp3JejnAqwMpf3NwgEn9Th7Z9mJeaFRK0NirL4_gGrpLN-OP1NLdds1akcQe_HJO9wKVdmewHDZwGcdeY0pWJ9vyVflJBle2p2j9iWhuW5z_0guT1AWhRox7ruGomu_UbwSCCOkZ-h9gScVQq0s#_=_", "http://localhost:8080/register/vnw/fb", null);
//    System.out.println(def);
//  }
}
