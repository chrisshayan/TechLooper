package com.techlooper.service;

import java.io.InputStream;

/**
 * Created by phuonghqh on 11/30/15.
 */
public interface ReportService {

  InputStream generateFinalChallengeReport(String challengeAuthorEmail, Long challengeId);
}
