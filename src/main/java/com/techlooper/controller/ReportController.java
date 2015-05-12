package com.techlooper.controller;

import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 5/11/15.
 */
@Controller
public class ReportController {

  @Resource
  private SalaryReviewRepository salaryReviewRepository;

  @RequestMapping(value = "renderSalaryReport/{language}/{salaryReviewId}")
  public String renderReport(@PathVariable String language, @PathVariable Long salaryReviewId, ModelMap model) {
    model.put("msg", "Hello Spring 4 Web MVC!");
    model.put("report", salaryReviewRepository.findOne(salaryReviewId).getSalaryReport());
    return "/jsp/salary-sharing." + language + ".jsp";
  }
}
