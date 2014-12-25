package com.techlooper.controller;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.*;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.SocialService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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

  @Resource
  private ApplicationContext applicationContext;

  @Resource
  private JsonConfigRepository jsonConfigRepository;

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
  public SocialResponse auth(@PathVariable SocialProvider provider, @RequestBody(required = false) Authentication auth) {
    SocialService service = applicationContext.getBean(provider + "Service", SocialService.class);
    AccessGrant accessGrant = service.getAccessGrant(auth.getCode());
    UserEntity userEntity = service.saveFootprint(accessGrant);
//    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("USER");
//    SecurityContextHolder.getContext()
//      .setAuthentication(new UsernamePasswordAuthenticationToken(
//              userEntity.getEmailAddress(), userEntity.getKey(), Arrays.asList(authority)));
    return SocialResponse.Builder.get()
      .withToken(accessGrant.getAccessToken())
      .withKey(userEntity.getKey()).build();
  }

  @ResponseBody
  @RequestMapping("/auth/oath1/{provider}")
  public SocialResponse auth1(@PathVariable SocialProvider provider, HttpServletResponse httpServletResponse,
                              @RequestParam(value = "oauth_token", required = false) String token,
                              @RequestParam(value = "oauth_verifier", required = false) String verifier) throws IOException {
    SocialService service = applicationContext.getBean(provider + "Service", SocialService.class);
    if (Optional.ofNullable(token).isPresent()) {
      AccessGrant accessGrant = service.getAccessGrant(token, verifier);
      UserEntity userEntity = service.saveFootprint(accessGrant);
      return SocialResponse.Builder.get()
        .withToken(accessGrant.getValue())
        .withKey(userEntity.getKey()).build();
    }
    AccessGrant accessGrant = service.getAccessGrant(null, null);
    httpServletResponse.sendRedirect(accessGrant.getAuthorizeUrl());
    return null;
  }
}
