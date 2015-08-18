package com.techlooper.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.Set;

/**
 * Created by phuonghqh on 8/18/15.
 */
@Document(indexName = "techlooper", type = "webinar")
public class WebinarEntity {

  @Id
  private Long createdDateTime = new Date().getTime();

  private String name;

  private String stateDate;

  private String endDate;

  private String description;

  private Set<String> attendees;

  private String organiser;

  private String calendarUrl;

  private String hangoutLink;

  public String getHangoutLink() {
    return hangoutLink;
  }

  public void setHangoutLink(String hangoutLink) {
    this.hangoutLink = hangoutLink;
  }

  public String getCalendarUrl() {
    return calendarUrl;
  }

  public void setCalendarUrl(String calendarUrl) {
    this.calendarUrl = calendarUrl;
  }

  public Long getCreatedDateTime() {
    return createdDateTime;
  }

  public void setCreatedDateTime(Long createdDateTime) {
    this.createdDateTime = createdDateTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStateDate() {
    return stateDate;
  }

  public void setStateDate(String stateDate) {
    this.stateDate = stateDate;
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

  public String getOrganiser() {
    return organiser;
  }

  public void setOrganiser(String organiser) {
    this.organiser = organiser;
  }
}
