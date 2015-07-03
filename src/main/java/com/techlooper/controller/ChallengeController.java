package com.techlooper.controller;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengeDetailDto;
import com.techlooper.model.ChallengeDto;
import com.techlooper.service.ChallengeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
            if (StringUtils.isNotEmpty(challengeEntity.getAuthorEmail())) {
                challengeService.sendPostChallengeEmailToEmployer(challengeEntity);
            }
            challengeService.sendPostChallengeEmailToTechloopies(challengeEntity);
        }

        return challengeEntity.getChallengeId();
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "/challenge/{challengeId}", method = RequestMethod.GET)
    public ChallengeDetailDto getChallengeDetail(@PathVariable Long challengeId) throws Exception {
        return challengeService.getChallengeDetail(challengeId);
    }

}
