package com.techlooper.model;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Map;

/**
 * Created by NguyenDangKhoa on 11/21/15.
 */
public class EmailRequestModel {

    private String templateName;

    private String subject;

    private Language language;

    private Map<String, Object> templateModel;

    private List<String> subjectVariableValues;

    private String recipientAddresses;

    private MimeMessage mailMessage;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Map<String, Object> getTemplateModel() {
        return templateModel;
    }

    public void setTemplateModel(Map<String, Object> templateModel) {
        this.templateModel = templateModel;
    }

    public List<String> getSubjectVariableValues() {
        return subjectVariableValues;
    }

    public void setSubjectVariableValues(List<String> subjectVariableValues) {
        this.subjectVariableValues = subjectVariableValues;
    }

    public String getRecipientAddresses() {
        return recipientAddresses;
    }

    public void setRecipientAddresses(String recipientAddresses) {
        this.recipientAddresses = recipientAddresses;
    }

    public MimeMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(MimeMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static class Builder {

        private EmailRequestModel instance = new EmailRequestModel();

        public Builder withTemplateName(String templateName) {
            instance.templateName = templateName;
            return this;
        }

        public Builder withLanguage(Language language) {
            instance.language = language;
            return this;
        }

        public Builder withTemplateModel(Map<String, Object> templateModel) {
            instance.templateModel = templateModel;
            return this;
        }

        public Builder withRecipientAddresses(String recipientAddresses) {
            instance.recipientAddresses = recipientAddresses;
            return this;
        }

        public Builder withSubjectVariableValues(List<String> subjectVariableValues) {
            instance.subjectVariableValues = subjectVariableValues;
            return this;
        }

        public Builder withMailMessage(MimeMessage mailMessage) {
            instance.mailMessage = mailMessage;
            return this;
        }

        public Builder withSubject(String subject) {
            instance.subject = subject;
            return this;
        }

        public EmailRequestModel build() {
            return instance;
        }
    }
}
