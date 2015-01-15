package com.techlooper.controller;

import com.techlooper.model.SocialRequest;
import com.techlooper.model.UserInfo;
import com.techlooper.model.VNWUserInfo;
import com.techlooper.service.UserService;
import com.techlooper.service.impl.VietnamWorksUserService;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by phuonghqh on 12/23/14.
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private TextEncryptor textEncryptor;

    @Resource
    private VietnamWorksUserService vietnamWorksUserService;

    @ResponseBody
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public List<FieldError> save(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
        if (result.getFieldErrorCount() > 0) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            boolean registerResult = false;
            if (userInfo.isRegisterVietnamworks()) {
                VNWUserInfo vnwUserInfo = new VNWUserInfo(userInfo.getEmailAddress(), userInfo.getFirstName(), userInfo.getLastName());
                registerResult = vietnamWorksUserService.register(vnwUserInfo);
            }
            userService.save(userInfo, registerResult);
        }
        return result.getFieldErrors();
    }


    @SendToUser("/queue/info")
    @MessageMapping("/user/findByKey")
    @ResponseBody
    @RequestMapping(value = "/user/findByKey", method = RequestMethod.POST)
    public UserInfo getUserInfo(@CookieValue("techlooper.key") String techlooperKey/*, @DestinationVariable String username */) {
        UserInfo userInfo = userService.findUserInfoByKey(techlooperKey);
        userInfo.getLoginSource();
        return userInfo;
    }

    @ResponseBody
    @RequestMapping(value = "/user/verifyUserLogin", method = RequestMethod.POST)
    public void verifyUserLogin(@RequestBody SocialRequest searchRequest, @CookieValue("techlooper.key") String techlooperKey, HttpServletResponse httpServletResponse) {
        if (!textEncryptor.encrypt(searchRequest.getEmailAddress()).equals(techlooperKey)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
