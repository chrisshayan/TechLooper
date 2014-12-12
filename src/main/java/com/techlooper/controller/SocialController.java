package com.techlooper.controller;

import com.techlooper.model.Authentication;
import com.techlooper.model.SocialAccessToken;
import com.techlooper.model.SocialConfig;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.SocialService;
import org.springframework.context.ApplicationContext;
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

  @RequestMapping("/getSocialConfig")
  @ResponseBody
  public SocialConfig getSocialConfig(@RequestParam SocialProvider provider) {
    return jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> provider == config.getProvider()).findFirst().get();
  }

  @RequestMapping("/auth/{provider}")
  @ResponseBody
  public SocialAccessToken auth(@PathVariable SocialProvider provider, @RequestBody Authentication auth) {
    SocialAccessToken token = new SocialAccessToken();
    Optional.ofNullable(applicationContext.getBean(provider + "Service", SocialService.class))
      .ifPresent(service -> token.setToken(service.getAccessToken(auth.getCode())));
    return token;
  }
}
