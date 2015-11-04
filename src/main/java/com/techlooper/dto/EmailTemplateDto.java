package com.techlooper.dto;

import com.techlooper.model.Language;

import java.util.List;

public class EmailTemplateDto {

    private Long templateId;

    private String templateName;

    private String subject;

    private String body;

    private Language language;

    private List<String> subjectVariables;

    private List<String> bodyVariables;

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<String> getSubjectVariables() {
        return subjectVariables;
    }

    public void setSubjectVariables(List<String> subjectVariables) {
        this.subjectVariables = subjectVariables;
    }

    public List<String> getBodyVariables() {
        return bodyVariables;
    }

    public void setBodyVariables(List<String> bodyVariables) {
        this.bodyVariables = bodyVariables;
    }
}
