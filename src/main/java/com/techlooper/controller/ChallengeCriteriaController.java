package com.techlooper.controller;

import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.model.ChallengeRegistrantCriteriaDto;
import com.techlooper.service.ChallengeCriteriaService;
import com.techlooper.service.ChallengeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by phuonghqh on 10/16/15.
 */
@RestController
public class ChallengeCriteriaController {

    @Resource
    private ChallengeCriteriaService challengeCriteriaService;

    @Resource
    private ChallengeService challengeService;

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "challenge/criteria", method = RequestMethod.POST)
    public ChallengeCriteriaDto saveChallengeCriteria(@RequestBody ChallengeCriteriaDto challengeCriteriaDto,
                                                      HttpServletRequest request, HttpServletResponse response) {
        String ownerEmail = request.getRemoteUser();
        if (challengeService.isOwnerOfChallenge(ownerEmail, challengeCriteriaDto.getChallengeId())) {
            ChallengeCriteriaDto result = challengeCriteriaService.saveChallengeCriteria(challengeCriteriaDto, ownerEmail);
            if (result == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return result;
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "challengeRegistrant/criteria", method = RequestMethod.POST)
    public ChallengeRegistrantCriteriaDto saveChallengeRegistrantCriteria(@RequestBody ChallengeRegistrantCriteriaDto registrantCriteriaDto,
                                                                          HttpServletRequest request, HttpServletResponse response) {
        ChallengeRegistrantCriteriaDto result = challengeCriteriaService.saveScoreChallengeRegistrantCriteria(
                registrantCriteriaDto, request.getRemoteUser());
        if (result == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return result;
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "challengeRegistrant/{registrantId}/criteria", method = RequestMethod.GET)
    public ChallengeRegistrantCriteriaDto findByChallengeRegistrantId(@PathVariable Long registrantId, HttpServletRequest request, HttpServletResponse response) {
        String ownerEmail = request.getRemoteUser();
        ChallengeRegistrantCriteriaDto result = challengeCriteriaService.findByChallengeRegistrantId(registrantId, ownerEmail);
        if (result == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return result;
    }
}
