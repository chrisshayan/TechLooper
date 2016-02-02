package com.techlooper.controller;

import com.techlooper.dto.ChallengeWinnerDto;
import com.techlooper.dto.JoiningRegistrantDto;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.DraftRegistrantEntity;
import com.techlooper.entity.vnw.VnwCompany;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.model.*;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.DraftRegistrantRepository;
import com.techlooper.service.*;
import com.techlooper.util.EmailValidator;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Resource
    private ChallengeEmailService challengeEmailService;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private UserController userController;

    @Resource
    private DraftRegistrantRepository draftRegistrantRepository;

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "/challenge/publish", method = RequestMethod.POST)
    public ChallengeResponse publishChallenge(@RequestBody ChallengeDto challengeDto, HttpServletRequest servletRequest) throws Exception {
        int responseCode = 0;
        String employerEmail = servletRequest.getRemoteUser();
        challengeDto.setAuthorEmail(employerEmail);
        ChallengeEntity challengeEntity = challengeService.postChallenge(challengeDto);
        boolean newEntity = challengeDto.getChallengeId() == null;
        if (newEntity) {
            if (challengeEntity != null) {
                if (EmailValidator.validate(challengeEntity.getAuthorEmail())) {
                    challengeEmailService.sendPostChallengeEmailToEmployer(challengeEntity);
                    challengeEmailService.sendEmailNotifyEmployerUpdateCriteria(challengeEntity);
                }
                challengeEmailService.sendPostChallengeEmailToTechloopies(challengeEntity, Boolean.TRUE);
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
            challengeEmailService.sendPostChallengeEmailToTechloopies(challengeEntity, Boolean.FALSE);
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
    public JoiningRegistrantDto joinChallenge(@RequestBody ChallengeRegistrantDto challengeRegistrantDto, HttpServletResponse response) throws Exception {
//        if (!EmailValidator.validate(challengeRegistrantDto.getRegistrantEmail())) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return 0L;
//        }
//        Long countRegistrants = challengeService.joinChallenge(challengeRegistrantDto);
//        if (countRegistrants == 0L) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        }
//        return countRegistrants;
        JoiningRegistrantDto joiningDto = challengeService.joinChallenge(challengeRegistrantDto);
        if (!joiningDto.isSucceedJoin()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return joiningDto;
    }

    @RequestMapping(value = "/challenge/list", method = RequestMethod.GET)
    public List<ChallengeDetailDto> listChallenges() throws Exception {
        ChallengeFilterCondition allChallengeFilterCondition = new ChallengeFilterCondition();
        return challengeService.findChallenges(allChallengeFilterCondition);
    }

    @RequestMapping(value = "/challenge/search", method = RequestMethod.POST)
    public List<ChallengeDetailDto> searchChallenges(@RequestBody ChallengeFilterCondition challengeFilterCondition) throws Exception {
        return challengeService.findChallenges(challengeFilterCondition);
    }

    @RequestMapping(value = "/challenge/stats", method = RequestMethod.GET)
    public ChallengeStatsDto getChallengeStatistics() throws Exception {
        ChallengeStatsDto challengeStatsDto = new ChallengeStatsDto();
        challengeStatsDto.setNumberOfChallenges(challengeService.getNumberOfChallenges());
        challengeStatsDto.setNumberOfRegistrants(challengeRegistrantService.getTotalNumberOfRegistrants());
        challengeStatsDto.setTotalPrizeAmount(challengeService.getTotalAmountOfPrizeValues());
        return challengeStatsDto;
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "/challenge/{id}", method = RequestMethod.DELETE)
    public void deleteChallengeById(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        if (!challengeService.deleteChallenge(id, request.getRemoteUser())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @RequestMapping(value = "/challenges/{challengeId}", method = RequestMethod.GET)
    public ChallengeDto findChallengeById(@PathVariable Long challengeId, HttpServletRequest request) throws Exception {
        ChallengeDto challengeDto = dozerMapper.map(challengeService.findChallengeById(challengeId), ChallengeDto.class);
        return challengeDto;
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "/challengeDetail/registrant", method = RequestMethod.POST)
    public ChallengeRegistrantDto saveRegistrant(@RequestBody ChallengeRegistrantDto dto, HttpServletRequest request) {
        return challengeRegistrantService.saveRegistrant(request.getRemoteUser(), dto);
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "challengeRegistrant/fullName/{registrantId}", method = RequestMethod.GET)
    public String getChallengeRegistrantFullName(@PathVariable Long registrantId) {
        ChallengeRegistrantEntity registrantEntity = challengeRegistrantRepository.findOne(registrantId);
        return registrantEntity.getRegistrantFirstName() + " " + registrantEntity.getRegistrantLastName();
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "/challenges/{challengeId}/registrantFunnel", method = RequestMethod.GET)
    public List<ChallengeRegistrantFunnelItem> getChallengeRegistrantFunnel(@PathVariable Long challengeId,
                                                                            HttpServletRequest request, HttpServletResponse response) {
        List<ChallengeRegistrantFunnelItem> funnel = new ArrayList<>();
        String ownerEmail = request.getRemoteUser();
        if (challengeService.isChallengeOwner(ownerEmail, challengeId)) {
            funnel = challengeRegistrantService.getChallengeRegistrantFunnel(challengeId, ownerEmail);
        } else {
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

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "challenge/registrant/winner", method = RequestMethod.POST)
    public Set<ChallengeWinner> saveWinner(@RequestBody ChallengeWinnerDto challengeWinner, HttpServletRequest request, HttpServletResponse response) {
        Set<ChallengeWinner> winners = challengeRegistrantService.saveWinner(challengeWinner, request.getRemoteUser());
        if (winners == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        return winners;
    }

    @RequestMapping(value = "challenge/{challengeId}/winners", method = RequestMethod.GET)
    public Set<ChallengeRegistrantDto> getWinners(@PathVariable Long challengeId) {
        return challengeRegistrantService.getChallengeWinners(challengeId);
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "challenge/updateVisibleWinner", method = RequestMethod.PUT)
    public ChallengeDetailDto updateVisibleWinner(@RequestBody ChallengeDetailDto challengeDetailDto, HttpServletRequest request, HttpServletResponse response) {
        UserProfileDto signinUser = userController.getUserProfile(request);
        challengeDetailDto = challengeService.updateVisibleWinner(challengeDetailDto, signinUser.getEmail());
        if (challengeDetailDto == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        return challengeDetailDto;
    }

    @RequestMapping(value = "challenge/saveDraftRegistrant", method = RequestMethod.POST)
    public DraftRegistrantEntity saveDraftRegistrant(@RequestBody DraftRegistrantEntity draftRegistrantEntity) {
        return challengeRegistrantService.saveDraftRegistrant(draftRegistrantEntity);
    }

    @RequestMapping(value = "challenge/draftRegistrant/{id}", method = RequestMethod.GET)
    public DraftRegistrantEntity getDraftRegistrant(@PathVariable Long id) {
        return draftRegistrantRepository.findOne(id);
    }

    @RequestMapping(value = "challenge/joined/{challengeId}/{email}", method = RequestMethod.GET)
    public boolean joinedChallenge(@PathVariable Long challengeId, @PathVariable String email) {
        return challengeRegistrantService.findRegistrantByChallengeIdAndEmail(challengeId, email, null) != null;
    }
}
