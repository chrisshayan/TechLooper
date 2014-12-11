package com.techlooper.controller;

import com.techlooper.model.Authentication;
import com.techlooper.model.Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by phuonghqh on 12/10/14.
 */
@Controller
public class SocialController {

  @RequestMapping("/auth")
  @ResponseBody
  public Token auth(@RequestBody Authentication auth) {
    return new Token(auth.getCode());
  }
}
