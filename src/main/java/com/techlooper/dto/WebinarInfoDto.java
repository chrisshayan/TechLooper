package com.techlooper.dto;

import com.techlooper.model.EmployerDto;
import com.techlooper.model.Language;
import com.techlooper.model.UserProfileDto;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by phuonghqh on 8/18/15.
 */
public class WebinarInfoDto implements Serializable {

    private Long createdDateTime;

    private String name;

    private String startDate;

    private String endDate;

    private String description;

    private Set<UserProfileDto> attendees;

    private String whatEvent;

    private UserProfileDto organiser;

    private EmployerDto company;

    private Language lang;

    public EmployerDto getCompany() {
        return company;
    }

    public void setCompany(EmployerDto company) {
        this.company = company;
    }

    public String getWhatEvent() {
        return whatEvent;
    }

    public void setWhatEvent(String whatEvent) {
        this.whatEvent = whatEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserProfileDto> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<UserProfileDto> attendees) {
        this.attendees = attendees;
    }

    public UserProfileDto getOrganiser() {
        return organiser;
    }

    public void setOrganiser(UserProfileDto organiser) {
        this.organiser = organiser;
    }

    public Long getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }
}
