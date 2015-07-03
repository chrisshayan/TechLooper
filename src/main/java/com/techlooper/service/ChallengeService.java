package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengeDetailDto;
import com.techlooper.model.ChallengeDto;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
public interface ChallengeService {

    ChallengeEntity savePostChallenge(ChallengeDto challengeDto) throws Exception;

    void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity)
            throws MessagingException, IOException, TemplateException;

    void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity)
            throws MessagingException, IOException, TemplateException;

    ChallengeDetailDto getChallengeDetail(Long challengeId);

    Long getNumberOfRegistrants(Long challengeId);

    void sendApplicationEmailToContestant(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity)
            throws MessagingException, IOException, TemplateException;

    void sendApplicationEmailToEmployer(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity)
            throws MessagingException, IOException, TemplateException;
}
