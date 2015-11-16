package com.techlooper.controller;

import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeSubmissionDto;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.ChallengeSubmissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by phuonghqh on 10/9/15.
 */
@RestController
public class ChallengeSubmissionController {

    @Resource
    private ChallengeSubmissionService challengeSubmissionService;

    @Resource
    private ChallengeService challengeService;

    @RequestMapping(value = "user/challengeSubmission", method = RequestMethod.POST)
    public ChallengeSubmissionEntity submitMyResult(@RequestBody ChallengeSubmissionDto challengeSubmissionDto) {
        return challengeSubmissionService.submitMyResult(challengeSubmissionDto);
    }

    @RequestMapping(value = "user/challengeSubmissionPhase/{registrantEmail}/{challengeId}", method = RequestMethod.GET)
    public ChallengePhaseEnum challengeSubmissionPhase(@PathVariable String registrantEmail, @PathVariable Long challengeId) {
        ChallengeRegistrantEntity registrantEntity =
                challengeService.findRegistrantByChallengeIdAndEmail(challengeId, registrantEmail);
        return (registrantEntity != null && registrantEntity.getActivePhase() != null) ?
                registrantEntity.getActivePhase() : ChallengePhaseEnum.REGISTRATION;
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challengeSubmission/markAsRead", method = RequestMethod.POST)
    public void markAsRead(@RequestBody ChallengeSubmissionDto challengeSubmissionDto,
                           HttpServletRequest request, HttpServletResponse response) {
        String ownerEmail = request.getRemoteUser();
        boolean isChallengeOwner = challengeService.isOwnerOfChallenge(ownerEmail, challengeSubmissionDto.getChallengeId());
        if (isChallengeOwner) {
            challengeSubmissionService.markChallengeSubmissionAsRead(challengeSubmissionDto);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

}
