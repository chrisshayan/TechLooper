package com.techlooper.controller;

import com.techlooper.dto.ResourceDto;
import com.techlooper.repository.elasticsearch.ProjectRepository;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.WebinarService;
import org.elasticsearch.rest.support.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by phuonghqh on 5/11/15.
 */
@Controller
public class SharingController {

  private final Logger LOGGER = LoggerFactory.getLogger(SharingController.class);

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

  @ResponseBody
  @RequestMapping(value = "resource/getUrlResponseCode", method = RequestMethod.POST)
  public Long getUrlResponseCode(@RequestBody ResourceDto resourceDto) {
    try {
      URL u = new URL(resourceDto.getUrl());
      HttpURLConnection huc = (HttpURLConnection) u.openConnection();
      huc.setDoOutput(true);
      huc.setRequestMethod("GET");
      huc.connect();
      OutputStream os = huc.getOutputStream();
      int rs = huc.getResponseCode();
      os.close();
      return Long.valueOf(rs);
    }
    catch (Exception e) {
      LOGGER.debug("Url not exist", e);
    }
    return 404L;
  }
}
