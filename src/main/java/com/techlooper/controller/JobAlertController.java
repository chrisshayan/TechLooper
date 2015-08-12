package com.techlooper.controller;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.service.JobAlertService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class JobAlertController {

    @Resource
    private JobAlertService jobAlertService;

    @ResponseBody
    @RequestMapping(value = "jobAlert/register", method = RequestMethod.POST)
    public JobAlertRegistrationEntity getCompany(@RequestBody JobAlertRegistration jobAlertRegistration) {
        return jobAlertService.registerJobAlert(jobAlertRegistration);
    }
}
