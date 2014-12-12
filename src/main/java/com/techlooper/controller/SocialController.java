package com.techlooper.controller;

import com.techlooper.model.Authentication;
import com.techlooper.model.SocialAccessToken;
import com.techlooper.model.SocialConfig;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.LinkedInService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by phuonghqh on 12/10/14.
 */
@Controller
public class SocialController {

  @Resource
  private LinkedInService linkedInService;

  @Resource
  private JsonConfigRepository jsonConfigRepository;

  @RequestMapping("/getClientId")
  @ResponseBody
  public String getClientId(@RequestParam String provider) {
    return jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> config.getProvider().equalsIgnoreCase(provider)).findFirst().get().getApiKey();
  }

  @RequestMapping("/auth")
  @ResponseBody
  public SocialAccessToken auth(@RequestBody Authentication auth) {
    return new SocialAccessToken(linkedInService.getAccessToken(auth.getCode()));
  }
}
