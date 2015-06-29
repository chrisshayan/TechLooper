package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
public interface ChallengeService {

    void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity, String mailSubject)
            throws MessagingException, IOException, TemplateException;

    void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity, String mailSubject)
            throws MessagingException, IOException, TemplateException;

}
