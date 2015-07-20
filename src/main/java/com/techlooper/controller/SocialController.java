package com.techlooper.controller;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.UserEntity;
import com.techlooper.entity.VnwUserProfile;
import com.techlooper.model.Authentication;
import com.techlooper.model.SocialConfig;
import com.techlooper.model.SocialProvider;
import com.techlooper.model.SocialResponse;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.service.SocialService;
import com.techlooper.service.VietnamWorksUserService;
import com.techlooper.service.impl.FacebookService;
import org.jasypt.util.text.TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by phuonghqh on 12/10/14.
 */
@Controller
public class SocialController {

  private static final Logger LOGGER = LoggerFactory.getLogger(SocialController.class);

  @Resource
  private ApplicationContext applicationContext;

  @Resource
  private JsonConfigRepository jsonConfigRepository;

  @Resource
  private TextEncryptor textEncryptor;

  @Resource
  private FacebookService facebookService;

  @Resource
  private ChallengeRegistrantRepository challengeRegistrantRepository;

  @Resource
  private VietnamWorksUserService vietnamWorksUserService;

//  @RequestMapping(value = "register/vnw/fb", method = RequestMethod.GET)
//  public void registerVnwUser(@RequestParam(required = false) String code, HttpServletResponse response) throws IOException {
//    if (code == null) {
//      response.sendRedirect("/#/?registerVnwUser=cancel");
//      return;
//    }
//
//    SocialConfig socialConfig = jsonConfigRepository.getSocialConfig().stream()
//      .filter(config -> SocialProvider.FACEBOOK_REGISTER == config.getProvider()).findFirst().get();
//
//    UserProfile userProfile;
//    try {
//      userProfile = facebookService.getUserProfile(code, socialConfig);
//    }
//    catch (Exception e) {
//      response.sendRedirect(socialConfig.getApiUrl().get("login"));
//      return;
//    }
//
//    ChallengeRegistrantEntity challengeRegistrantEntity = challengeRegistrantRepository.save(
//      new ChallengeRegistrantEntity(new Date().getTime(), userProfile.getEmail(), userProfile.getFirstName(), userProfile.getLastName()));
//
//    if (StringUtils.hasText(userProfile.getEmail())) {
//      try {
//        vietnamWorksUserService.register(VnwUserProfile.VnwUserProfileBuilder.vnwUserProfile()
//          .withEmail(userProfile.getEmail()).withFirstname(userProfile.getFirstName()).withLastname(userProfile.getLastName()).build());
//      }
//      catch (Exception e) {
//        LOGGER.debug("Error register Vietnamworks", e);
//      }
//    }
//
//    response.sendRedirect("/#/?registerVnwUser=" + challengeRegistrantEntity.getRegistrantId());
//  }

  @RequestMapping(value = "register/vnw/fb", method = RequestMethod.GET)
  public void registerVnwUserFromFB(@RequestParam(required = false) String code, HttpServletResponse response) throws IOException {
    if (code == null) {
      response.sendRedirect("/#/?action=cancel");
      return;
    }

    SocialConfig socialConfig = jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> SocialProvider.FACEBOOK_REGISTER == config.getProvider()).findFirst().get();

    UserProfile userProfile;
    try {
      userProfile = facebookService.getUserProfile(code, socialConfig);
    }
    catch (Exception e) {
      response.sendRedirect(socialConfig.getApiUrl().get("login"));
      return;
    }

    if (StringUtils.hasText(userProfile.getEmail())) {
      try {
        vietnamWorksUserService.register(VnwUserProfile.VnwUserProfileBuilder.vnwUserProfile()
          .withEmail(userProfile.getEmail()).withFirstname(userProfile.getFirstName()).withLastname(userProfile.getLastName()).build());
      }
      catch (Exception e) {
        LOGGER.debug("Error register Vietnamworks", e);
      }
    }

    if (StringUtils.hasText(userProfile.getEmail())) {
      response.sendRedirect(String.format("/#/?action=success&firstName=%s&lastName=%s&email=%s",
        userProfile.getFirstName(), userProfile.getLastName(), Base64.getEncoder().encodeToString(userProfile.getEmail().getBytes())));
    }
    else {
      response.sendRedirect(String.format("/#/?action=success&firstName=%s&lastName=%s",
        userProfile.getFirstName(), userProfile.getLastName()));
    }
  }

  @ResponseBody
  @RequestMapping(value = "social/{provider}/loginUrl", method = RequestMethod.GET)
  public String getFacebookLoginUrl(@PathVariable SocialProvider provider) {
    SocialConfig socialConfig = jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> provider == config.getProvider()).findFirst().get();
    return socialConfig.getApiUrl().get("login");
  }


  @ResponseBody
  @RequestMapping("/getSocialConfig")
  public List<SocialConfig> getSocialConfig(@RequestParam("providers[]") List<SocialProvider> providers) {
    List<SocialConfig> configs = new ArrayList<>();
    providers.forEach(prov ->
        configs.add(jsonConfigRepository.getSocialConfig().stream()
          .filter(config -> prov == config.getProvider()).findFirst().get())
    );
    return configs;
  }

  @ResponseBody
  @RequestMapping("/auth/{provider}")
  public SocialResponse auth(@PathVariable SocialProvider provider,
                             @CookieValue(value = "techlooper.key", required = false) String key,
                             @RequestBody(required = false) Authentication auth) {
    SocialService service = applicationContext.getBean(provider + "Service", SocialService.class);
    AccessGrant accessGrant = service.getAccessGrant(auth.getCode());
    UserEntity userEntity = StringUtils.hasText(key) ? service.saveFootprint(accessGrant, key) : service.saveFootprint(accessGrant);
    return SocialResponse.Builder.get()
      .withToken(accessGrant.getAccessToken())
      .withKey(textEncryptor.encrypt(userEntity.key())).build();
  }

  @ResponseBody
  @RequestMapping("/auth/oath1/{provider}")
  public SocialResponse auth1(@PathVariable SocialProvider provider, HttpServletResponse httpServletResponse,
                              @CookieValue(value = "techlooper.key", required = false) String key,
                              @RequestParam(value = "oauth_token", required = false) String token,
                              @RequestParam(value = "oauth_verifier", required = false) String verifier) throws IOException {
    SocialService service = applicationContext.getBean(provider + "Service", SocialService.class);
    if (Optional.ofNullable(token).isPresent()) {
      AccessGrant accessGrant = service.getAccessGrant(token, verifier);
      UserEntity userEntity = StringUtils.hasText(key) ? service.saveFootprint(accessGrant, key) : service.saveFootprint(accessGrant);
      return SocialResponse.Builder.get()
        .withToken(accessGrant.getValue())
        .withKey(textEncryptor.encrypt(userEntity.key())).build();
    }
    AccessGrant accessGrant = service.getAccessGrant(null, null);
    httpServletResponse.sendRedirect(accessGrant.getAuthorizeUrl());
    return null;
  }
}