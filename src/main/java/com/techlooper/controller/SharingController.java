package com.techlooper.controller;

import com.techlooper.dto.ResourceDto;
import com.techlooper.repository.elasticsearch.ProjectRepository;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.ReportService;
import com.techlooper.service.WebinarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

  @Resource
  private ReportService reportService;

  @Resource
  private ViewResolver viewResolver;

  @RequestMapping(value = "renderSalaryReport/{language}/{salaryReviewId}")
  public String renderReport(@PathVariable String language, @PathVariable Long salaryReviewId, ModelMap model) {
    model.put("report", salaryReviewRepository.findOne(salaryReviewId).getSalaryReport());
    return "/jsp/salary-sharing." + language + ".jsp";
  }

  @RequestMapping(value = "shareChallenge/{language}/{id}")
  public String renderChallenge(@PathVariable String language, @PathVariable Long id, ModelMap model) {
    model.put("challenge", challengeService.getChallengeDetail(id, ""));
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
      HttpURLConnection.setFollowRedirects(false);
      String url = resourceDto.getUrl().trim();
      url = (url.startsWith("https://") || url.startsWith("http://")) ? url : "http://" + url;
      HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
      con.setRequestMethod("HEAD");
      int responseCode = con.getResponseCode();
      return Long.valueOf(responseCode);
    }
    catch (Exception e) {
      LOGGER.debug("Url not exist", e);
    }
    return 404L;
  }

  @RequestMapping(value = "report/challenge/final/{challengeId}")
  public void renderFinalChallengeReport(@PathVariable Long challengeId, HttpServletRequest request, HttpServletResponse response) throws IOException {
    ByteArrayOutputStream os = reportService.generateFinalChallengeReport(request.getRemoteUser(), challengeId);
    if (os == null) {
      response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
      return;
    }

    byte[] data = os.toByteArray();
    response.setContentType("application/pdf");
    response.setHeader("Content-disposition", "attachment;filename=report.pdf");
    response.setContentLength(data.length);
    response.getOutputStream().write(data);
    response.getOutputStream().flush();
  }
}
