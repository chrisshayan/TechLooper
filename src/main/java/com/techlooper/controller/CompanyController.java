package com.techlooper.controller;

import com.techlooper.entity.CompanyEntity;
import com.techlooper.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 4/1/15.
 */
@Controller
public class CompanyController {

  @Resource
  private CompanyService companyService;

  @ResponseBody
  @RequestMapping(value = "company/{companyName}", method = RequestMethod.GET)
  public CompanyEntity getCompany(@PathVariable String companyName) {
    return companyService.findByName(companyName);
  }
}
