package com.techlooper.dto;

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

    private Set<String> attendees;

    private String whatEvent;

    private UserProfileDto organiser;

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

    public Set<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(Set<String> attendees) {
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
}
