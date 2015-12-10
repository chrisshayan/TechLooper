package com.techlooper.service;

import com.techlooper.model.Language;

import java.io.ByteArrayOutputStream;

/**
 * Created by phuonghqh on 11/30/15.
 */
public interface ReportService {

  ByteArrayOutputStream generateFinalChallengeReport(String challengeAuthorEmail, Long challengeId, Language language);
}
