package com.techlooper.controller;

import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.ChallengeService;
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

  @RequestMapping(value = "renderSalaryReport/{language}/{salaryReviewId}")
  public String renderReport(@PathVariable String language, @PathVariable Long salaryReviewId, ModelMap model) {
    model.put("report", salaryReviewRepository.findOne(salaryReviewId).getSalaryReport());
    return "/jsp/salary-sharing." + language + ".jsp";
  }

  @RequestMapping(value = "shareChallenge/{language}/{id}")
  public String renderChallenge(@PathVariable String language, @PathVariable Long id, ModelMap model) {
    model.put("challenge", challengeService.getChallengeDetail(id));
    model.put("lang", language);
    return "/jsp/challenge-sharing.jsp";
  }
}
