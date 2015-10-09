package com.techlooper.service;

import com.techlooper.model.EmailContent;

/**
 * Created by phuonghqh on 10/1/15.
 */
public interface EmailService {

  boolean sendEmail(EmailContent emailContent);
}
