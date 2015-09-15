package com.techlooper.controller;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.model.ChallengeDetailDto;
import com.techlooper.model.ChallengeDto;
import com.techlooper.model.ChallengeStatsDto;
import com.techlooper.service.ChallengeService;
import com.techlooper.util.EmailValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class ChallengeController {

  @Resource
  private ChallengeService challengeService;

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "/challenge/publish", method = RequestMethod.POST)
  public long publishChallenge(@RequestBody ChallengeDto challengeDto, HttpServletRequest servletRequest) throws Exception {
    challengeDto.setAuthorEmail(servletRequest.getRemoteUser());
    ChallengeEntity challengeEntity = challengeService.savePostChallenge(challengeDto);
    if (challengeEntity.getChallengeId() != -1L) {
      if (EmailValidator.validate(challengeEntity.getAuthorEmail())) {
        challengeService.sendPostChallengeEmailToEmployer(challengeEntity);
      }
      challengeService.sendPostChallengeEmailToTechloopies(challengeEntity);
    }

    return challengeEntity.getChallengeId();
  }

  @RequestMapping(value = "/challenge/{challengeId}", method = RequestMethod.GET)
  public ChallengeDetailDto getChallengeDetail(@PathVariable Long challengeId) throws Exception {
    return challengeService.getChallengeDetail(challengeId);
  }

  @RequestMapping(value = "/challenge/join", method = RequestMethod.POST)
  public long joinChallenge(@RequestBody ChallengeRegistrantDto joinChallenge) throws Exception {
    return challengeService.joinChallenge(joinChallenge);
  }

  @RequestMapping(value = "/challenge/list", method = RequestMethod.GET)
  public List<ChallengeDetailDto> listChallenges() throws Exception {
    return challengeService.listChallenges();
  }

  @RequestMapping(value = "/challenge/stats", method = RequestMethod.GET)
  public ChallengeStatsDto getChallengeStatistics() throws Exception {
    ChallengeStatsDto challengeStatsDto = new ChallengeStatsDto();
    challengeStatsDto.setNumberOfChallenges(challengeService.getTotalNumberOfChallenges());
    challengeStatsDto.setNumberOfRegistrants(challengeService.getTotalNumberOfRegistrants());
    challengeStatsDto.setTotalPrizeAmount(challengeService.getTotalAmountOfPrizeValues());
    return challengeStatsDto;
  }


  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "/challenge/{id}", method = RequestMethod.DELETE)
  public void deleteChallengeById(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
    if (!challengeService.delete(id, request.getRemoteUser())) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
  }
}
