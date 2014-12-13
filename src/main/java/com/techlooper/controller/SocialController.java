package com.techlooper.controller;

import com.techlooper.entity.UserEntity;
import com.techlooper.model.*;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.SocialService;
import com.techlooper.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by phuonghqh on 12/10/14.
 */
@Controller
public class SocialController {

  @Resource
  private ApplicationContext applicationContext;

  @Resource
  private JsonConfigRepository jsonConfigRepository;

  @Resource
  private SocialService defaultSocialService;

  @Resource
  private UserService userService;

  @SendTo("/topic/userInfo/email")
  @MessageMapping("/userInfo/email")
  public UserInfo getUserInfo(SocialRequest searchRequest) {
    return userService.findByKey(searchRequest.getKey());
  }

  @RequestMapping("/getSocialConfig")
  @ResponseBody
  public SocialConfig getSocialConfig(@RequestParam SocialProvider provider) {
    return jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> provider == config.getProvider()).findFirst().get();
  }

  @RequestMapping("/auth/{provider}")
  @ResponseBody
  public SocialResponse auth(@PathVariable SocialProvider provider, @RequestBody Authentication auth) {
    SocialService service = Optional.ofNullable(applicationContext.getBean(provider + "Service", SocialService.class)).orElse(defaultSocialService);
    AccessGrant accessGrant = service.getAccessGrant(auth.getCode());
    UserEntity userEntity = service.persistProfile(accessGrant);
    return SocialResponse.Builder.get()
      .withToken(accessGrant.getAccessToken())
      .withKey(userEntity.getKey()).build();
  }
}
