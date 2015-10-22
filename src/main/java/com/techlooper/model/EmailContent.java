package com.techlooper.model;

import javax.mail.Address;
import java.io.Serializable;

/**
 * Created by phuonghqh on 10/1/15.
 */
public class EmailContent implements Serializable {

    private String subject;

    private String content;

    private Address[] recipients;

    private Language language;

    private Address[] replyTo;

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Address[] getRecipients() {
        return recipients;
    }

    public void setRecipients(Address[] recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Address[] getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Address[] replyTo) {
        this.replyTo = replyTo;
    }
}
