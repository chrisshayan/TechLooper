package com.techlooper.controller;

import com.techlooper.model.ProjectDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProjectController {

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "/project/post", method = RequestMethod.POST)
    public long postProject(@RequestBody ProjectDto projectDto, HttpServletRequest servletRequest) throws Exception {
        projectDto.setAuthorEmail(servletRequest.getRemoteUser());
        return 1;
    }

}
