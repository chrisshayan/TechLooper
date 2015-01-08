package com.techlooper.controller;

import com.techlooper.model.SocialRequest;
import com.techlooper.model.UserInfo;
import com.techlooper.service.UserService;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by phuonghqh on 12/23/14.
 */
@Controller
public class UserController {

  @Resource
  private UserService userService;

  @Resource
  private TextEncryptor textEncryptor;

  @ResponseBody
  @RequestMapping(value = "/user/save", method = RequestMethod.POST)
  public List<FieldError> save(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
    if (result.getFieldErrorCount() > 0) {
      httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    else {
      userService.save(userInfo);
    }
    return result.getFieldErrors();
  }


  @SendToUser("/queue/info")
  @MessageMapping("/user/findByKey")
  @ResponseBody
  @RequestMapping(value = "/user/findByKey", method = RequestMethod.POST)
  public UserInfo getUserInfo(@RequestBody @Valid SocialRequest searchRequest/*, @DestinationVariable String username */) {
    UserInfo userInfo = userService.findUserInfoByKey(searchRequest.getKey());
    userInfo.getLoginSource();
    return userInfo;
  }

  @ResponseBody
  @RequestMapping(value = "/user/verifyUserLogin", method = RequestMethod.POST)
  public void verifyUserLogin(@RequestBody @Valid SocialRequest searchRequest, HttpServletResponse httpServletResponse) {
    if (!textEncryptor.decrypt(searchRequest.getKey()).equals(searchRequest.getEmailAddress())) {
      httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
  }
}
