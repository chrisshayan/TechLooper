package com.techlooper.controller;

import com.techlooper.model.SocialRequest;
import com.techlooper.model.UserInfo;
import com.techlooper.service.UserService;
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

/**
 * Created by phuonghqh on 12/23/14.
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public List<FieldError> save(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
        if (result.getFieldErrorCount() > 0) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            userService.save(userInfo);
        }
        return result.getFieldErrors();
    }


    //  @SendTo("/topic/userInfo/key")
//  @MessageMapping("/userInfo/key")
    @ResponseBody
    @RequestMapping("/user")
    public UserInfo getUserInfo(@RequestBody SocialRequest searchRequest) {
        return userService.findUserInfoByKey(searchRequest.getKey());
    }

}
