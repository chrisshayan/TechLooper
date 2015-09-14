package com.techlooper.controller;

import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.repository.elasticsearch.ProjectRepository;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.ProjectService;
import com.techlooper.service.WebinarService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 5/11/15.
 */
@Controller
public class SharingController {

  @Resource
  private SalaryReviewRepository salaryReviewRepository;

  @Resource
  private ChallengeService challengeService;

  @Value("${web.baseUrl}")
  private String baseUrl;

  @Resource
  private WebinarService webinarService;

  @Resource
  private ProjectRepository projectRepository;

  @RequestMapping(value = "renderSalaryReport/{language}/{salaryReviewId}")
  public String renderReport(@PathVariable String language, @PathVariable Long salaryReviewId, ModelMap model) {
    model.put("report", salaryReviewRepository.findOne(salaryReviewId).getSalaryReport());
    return "/jsp/salary-sharing." + language + ".jsp";
  }

  @RequestMapping(value = "shareChallenge/{language}/{id}")
  public String renderChallenge(@PathVariable String language, @PathVariable Long id, ModelMap model) {
    model.put("challenge", challengeService.getChallengeDetail(id));
    model.put("lang", language);
    model.put("baseUrl", baseUrl);
    return "/jsp/challenge-sharing.jsp";
  }

  @RequestMapping(value = "shareWebinar/{language}/{id}")
  public String renderWebinar(@PathVariable String language, @PathVariable Long id, ModelMap model) {
    model.put("webinar", webinarService.findWebinarById(id));
    model.put("lang", language);
    model.put("baseUrl", baseUrl);
    return "/jsp/webinar-sharing.jsp";
  }

  @RequestMapping(value = "shareFreelancerProject/{language}/{id}")
  public String renderFreelancerProject(@PathVariable String language, @PathVariable Long id, ModelMap model) {
    model.put("freelancerProject", projectRepository.findOne(id));
    model.put("lang", language);
    model.put("baseUrl", baseUrl);
    return "/jsp/freelancer-sharing.jsp";
  }
}
