package com.techlooper.config.web.sec;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.UserProfile;
import com.techlooper.entity.VnwUserProfile;
import com.techlooper.entity.vnw.RoleName;
import com.techlooper.model.Language;
import com.techlooper.model.SocialProvider;
import com.techlooper.model.UserProfileDto;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.SocialService;
import com.techlooper.service.UserService;
import com.techlooper.service.VietnamWorksUserService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by phuonghqh on 7/28/15.
 */
public class SocialAuthenticationProvider implements AuthenticationProvider {

  @Resource
  private ApplicationContext applicationContext;

  @Resource
  protected Mapper dozerBeanMapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(SocialAuthenticationProvider.class);

  @Resource
  private UserService userService;

  @Resource
  private VietnamWorksUserService vietnamWorksUserService;

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UsernamePasswordAuthenticationToken socialAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
    SocialProvider socialProvider = SocialProvider.valueOf(socialAuthenticationToken.getCredentials().toString());
    String code = authentication.getPrincipal().toString();
    SocialService socialService = (SocialService) applicationContext.getBean(socialProvider.name() + "Service");

    UserProfile userProfile = null;
    UserProfileDto userProfileDto = null;
    try {
      AccessGrant accessGrant = socialService.getAccessGrant(code);
      userProfile = socialService.getProfile(accessGrant);
      userProfileDto = dozerBeanMapper.map(userProfile, UserProfileDto.class);
      userProfileDto.setRoleName(RoleName.JOB_SEEKER);
    }
    catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
    }

    if (userProfile != null) {
      if (StringUtils.hasText(userProfileDto.getEmail())) {
        try {
          userService.sendOnBoardingEmail(userProfileDto.getEmail(), Language.en);
        }
        catch (Exception e) {
          userProfileDto.getEmail();
        }
      }

      try {
        VnwUserProfile vnwUserProfile = VnwUserProfile.VnwUserProfileBuilder.vnwUserProfile()
          .withEmail(userProfileDto.getEmail())
          .withFirstname(userProfileDto.getFirstName())
          .withLastname(userProfileDto.getLastName()).build();
        vietnamWorksUserService.register(vnwUserProfile);
      }
      catch (Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
      }

      return new UsernamePasswordAuthenticationToken(userProfileDto, null,
        Arrays.asList(new SimpleGrantedAuthority(RoleName.JOB_SEEKER.name())));
    }

    return null;
  }

  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
