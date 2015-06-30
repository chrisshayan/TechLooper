package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengeDto;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
public interface ChallengeService {

    ChallengeEntity savePostChallenge(ChallengeDto challengeDto);

    void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity)
            throws MessagingException, IOException, TemplateException;

    void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity)
            throws MessagingException, IOException, TemplateException;

}
