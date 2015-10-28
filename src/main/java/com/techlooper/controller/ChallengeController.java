package com.techlooper.controller;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.vnw.VnwCompany;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.EmployerService;
import com.techlooper.service.LeadAPIService;
import com.techlooper.util.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class ChallengeController {

  private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeController.class);

  @Resource
  private ChallengeService challengeService;

  @Resource
  private EmployerService employerService;

  @Resource
  private LeadAPIService leadAPIService;

  @Resource
  private ChallengeRegistrantRepository challengeRegistrantRepository;

  @Resource
  private ChallengeRegistrantService challengeRegistrantService;

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "/challenge/publish", method = RequestMethod.POST)
  public ChallengeResponse publishChallenge(@RequestBody ChallengeDto challengeDto, HttpServletRequest servletRequest) throws Exception {
    int responseCode = 0;
    String employerEmail = servletRequest.getRemoteUser();
    challengeDto.setAuthorEmail(employerEmail);
    ChallengeEntity challengeEntity = challengeService.savePostChallenge(challengeDto);
    boolean newEntity = challengeDto.getChallengeId() == null;
    if (newEntity) {
      if (challengeEntity != null) {
        if (EmailValidator.validate(challengeEntity.getAuthorEmail())) {
          challengeService.sendPostChallengeEmailToEmployer(challengeEntity);
        }
        challengeService.sendPostChallengeEmailToTechloopies(challengeEntity, Boolean.TRUE);
      }

      // Call Lead Management API to create new lead on CRM system
      try {
        VnwUser employer = employerService.findEmployerByUsername(employerEmail);
        VnwCompany company = employerService.findCompanyById(employer.getCompanyId());
        if (employer != null && company != null) {
          responseCode = leadAPIService.createNewLead(
            employer, company, LeadEventEnum.POST_CHALLENGE, challengeEntity.getChallengeName());

          String logMessage = "Create Lead API Response Code : %d ,EmployerID : %d ,CompanyID : %d";
          LOGGER.info(String.format(logMessage, responseCode, employer.getUserId(), company.getCompanyId()));
        }
      }
      catch (Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
      }
    }
    else {
      challengeService.sendPostChallengeEmailToTechloopies(challengeEntity, Boolean.FALSE);
    }

    return new ChallengeResponse(challengeEntity.getChallengeId(), responseCode);
  }

  @RequestMapping(value = "/challenge/{challengeId}", method = RequestMethod.GET)
  public ChallengeDetailDto getChallengeDetail(@PathVariable Long challengeId, HttpServletRequest request, HttpServletResponse response) throws Exception {
    ChallengeDetailDto challengeDetail = challengeService.getChallengeDetail(challengeId, request.getRemoteUser());
    if (challengeDetail == null) response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return challengeDetail;
  }

  @RequestMapping(value = "/challenge/join", method = RequestMethod.POST)
  public long joinChallenge(@RequestBody ChallengeRegistrantDto joinChallenge, HttpServletResponse response) throws Exception {
    if (!EmailValidator.validate(joinChallenge.getRegistrantEmail())) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return 0L;
    }
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

  @RequestMapping(value = "/challenges/{challengeId}", method = RequestMethod.GET)
  public ChallengeDto findChallengeById(@PathVariable Long challengeId, HttpServletRequest request) throws Exception {
    return challengeService.findChallengeById(challengeId, request.getRemoteUser());
  }

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "/challenges/{challengeId}/registrants", method = RequestMethod.POST)
  public Set<ChallengeRegistrantDto> getRegistrantsById(@PathVariable Long challengeId, @RequestBody RegistrantFilterCondition condition,
                                                        HttpServletRequest request, HttpServletResponse response) throws ParseException {
    String owner = request.getRemoteUser();
    if (!challengeService.isOwnerOfChallenge(owner, challengeId)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return null;
    }
    condition.setAuthorEmail(owner);
    return challengeService.findRegistrantsByOwner(condition);
  }


  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "/challengeDetail/registrant", method = RequestMethod.POST)
  public ChallengeRegistrantDto saveRegistrant(@RequestBody ChallengeRegistrantDto dto, HttpServletRequest request) {
    return challengeService.saveRegistrant(request.getRemoteUser(), dto);
  }

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "challengeRegistrant/fullName/{registrantId}", method = RequestMethod.GET)
  public String getChallengeRegistrant(@PathVariable Long registrantId) {
    ChallengeRegistrantEntity registrantEntity = challengeRegistrantRepository.findOne(registrantId);
    return registrantEntity.getRegistrantFirstName() + " " + registrantEntity.getRegistrantLastName();
  }

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "/challenges/{challengeId}/registrantFunnel", method = RequestMethod.GET)
  public List<ChallengeRegistrantFunnelItem> getChallengeRegistrantFunnel(@PathVariable Long challengeId,
                                                                          HttpServletRequest request, HttpServletResponse response) {
    List<ChallengeRegistrantFunnelItem> funnel = new ArrayList<>();
    if (challengeService.isOwnerOfChallenge(request.getRemoteUser(), challengeId)) {
      funnel = challengeService.getChallengeRegistrantFunnel(challengeId);
    }
    else {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
    return funnel;
  }

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "challenge/{challengeId}/registrants/{phase}", method = RequestMethod.GET)
  public Set<ChallengeRegistrantDto> getChallengeRegistrantsByPhase(@PathVariable Long challengeId, @PathVariable ChallengePhaseEnum phase,
                                                                    HttpServletRequest request, HttpServletResponse response) {
    Set<ChallengeRegistrantDto> registrants = challengeRegistrantService.findRegistrantsByChallengeIdAndPhase(challengeId, phase, request.getRemoteUser());
    if (registrants == null) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
    return registrants;
  }
}
