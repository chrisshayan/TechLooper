package com.techlooper.controller;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.vnw.VnwCompany;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.model.*;
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
import java.util.List;

@RestController
public class ChallengeController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeController.class);

    @Resource
    private ChallengeService challengeService;

    @Resource
    private EmployerService employerService;

    @Resource
    private LeadAPIService leadAPIService;

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
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        } else {
            challengeService.sendPostChallengeEmailToTechloopies(challengeEntity, Boolean.FALSE);
        }

        return new ChallengeResponse(challengeEntity.getChallengeId(), responseCode);
    }

    @RequestMapping(value = "/challenge/{challengeId}", method = RequestMethod.GET)
    public ChallengeDetailDto getChallengeDetail(@PathVariable Long challengeId, HttpServletResponse response) throws Exception {
        ChallengeDetailDto challengeDetail = challengeService.getChallengeDetail(challengeId);
        if (challengeDetail == null) response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return challengeDetail;
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

    @RequestMapping(value = "/challenges/{challengeId}", method = RequestMethod.GET)
    public ChallengeDto findChallengeById(@PathVariable Long challengeId) throws Exception {
        return challengeService.findChallengeById(challengeId);
    }
}
