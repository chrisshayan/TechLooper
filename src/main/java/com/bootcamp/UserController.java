package com.bootcamp;

import com.google.common.io.Files;
import com.techlooper.util.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by phuonghqh on 1/29/15.
 */
@Controller("/bootcamp")
public class UserController {

  @ResponseBody
  @RequestMapping(value = "/users",  method = RequestMethod.POST)
  public void save(@RequestBody BootcampUserInfo bootcampUserInfo, HttpServletResponse httpServletResponse) throws IOException {
    File jsonFile = new File("bootcamp-users.json");
    StringBuilder jsonBuilder = new StringBuilder();
    if (jsonFile.exists()) {
      Files.readLines(jsonFile, StandardCharsets.UTF_8).stream().forEach(jsonBuilder::append);
    }

    List<BootcampUserInfo> users = JsonUtils.toList(jsonBuilder.toString(), BootcampUserInfo.class).orElse(new ArrayList<>());
    users.add(bootcampUserInfo);
    JsonUtils.getObjectMapper().writeValue(jsonFile, users);
    httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}
