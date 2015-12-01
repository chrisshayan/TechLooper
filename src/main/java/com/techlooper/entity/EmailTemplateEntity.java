package com.techlooper.entity;

import com.techlooper.model.Language;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Boolean;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

@Document(indexName = "techlooper", type = "emailTemplate")
public class EmailTemplateEntity {

    @Id
    private Long templateId;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String templateName;

    @Field(type = String)
    private String subject;

    @Field(type = String)
    private List<String> subjectVariables;

    @Field(type = String)
    private List<String> replyToAddresses;

    @Field(type = String)
    private List<String> recipientAddresses;

    @Field(type = String)
    private String fromAddress;

    @Field(type = String)
    private String body;

    @Field(type = String)
    private List<String> bodyVariables;

    @Field(type = Boolean)
    private Boolean isEnable;

    @Field(type = String, index = FieldIndex.not_analyzed)
    private Language language;

    @Field(type = String)
    private String titleEN;

    @Field(type = String)
    private String titleVI;

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

    public List<String> getSubjectVariables() {
        return subjectVariables;
    }

    public void setSubjectVariables(List<String> subjectVariables) {
        this.subjectVariables = subjectVariables;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getBodyVariables() {
        return bodyVariables;
    }

    public void setBodyVariables(List<String> bodyVariables) {
        this.bodyVariables = bodyVariables;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public List<String> getReplyToAddresses() {
        return replyToAddresses;
    }

    public void setReplyToAddresses(List<String> replyToAddresses) {
        this.replyToAddresses = replyToAddresses;
    }

    public List<String> getRecipientAddresses() {
        return recipientAddresses;
    }

    public void setRecipientAddresses(List<String> recipientAddresses) {
        this.recipientAddresses = recipientAddresses;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getTitleVI() {
        return titleVI;
    }

    public void setTitleVI(String titleVI) {
        this.titleVI = titleVI;
    }
}
