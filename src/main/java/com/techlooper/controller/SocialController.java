package com.techlooper.controller;

import com.techlooper.model.Authentication;
import com.techlooper.model.SocialAccessToken;
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

  @RequestMapping("/getClientId")
  @ResponseBody
  public String getClientId(@RequestParam String provider) {
    return jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> config.getProvider().equalsIgnoreCase(provider)).findFirst().get().getApiKey();
  }

  @RequestMapping("/auth/{provider}")
  @ResponseBody
  public SocialAccessToken auth(@PathVariable String provider, @RequestBody Authentication auth) {
    SocialAccessToken token = new SocialAccessToken();
    Optional.ofNullable(applicationContext.getBean(provider + "Service", SocialService.class))
      .ifPresent(service -> token.setToken(service.getAccessToken(auth.getCode())));
    return token;
  }
}
