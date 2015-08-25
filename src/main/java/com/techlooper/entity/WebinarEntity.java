package com.techlooper.entity;

import com.techlooper.model.UserProfileDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * Created by phuonghqh on 8/18/15.
 */
@Document(indexName = "techlooper", type = "webinar")
public class WebinarEntity {

  @Id
  private Long createdDateTime = new Date().getTime();

  @Field(type = FieldType.String)
  private String name;

  @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy hh:mm a")
  private String startDate;

  @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy hh:mm a")
  private String endDate;

  @Field(type = FieldType.String)
  private String description;

  @Field(type = FieldType.String)
  private Collection<String> attendees;

  @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
  private UserProfileDto organiser;

  @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
  private String where = "Google Hangout";

  @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
  private String calendarUrl;

  @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
  private String hangoutLink;

  @Field(type = FieldType.String)
  private String whatEvent;

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

  public Collection<String> getAttendees() {
    return attendees;
  }

  public void setAttendees(Collection<String> attendees) {
    this.attendees = attendees;
  }

  public UserProfileDto getOrganiser() {
    return organiser;
  }

  public void setOrganiser(UserProfileDto organiser) {
    this.organiser = organiser;
  }

  public String getWhere() {
    return where;
  }

  public void setWhere(String where) {
    this.where = where;
  }

  public String getCalendarUrl() {
    return calendarUrl;
  }

  public void setCalendarUrl(String calendarUrl) {
    this.calendarUrl = calendarUrl;
  }

  public String getHangoutLink() {
    return hangoutLink;
  }

  public void setHangoutLink(String hangoutLink) {
    this.hangoutLink = hangoutLink;
  }

  public String getWhatEvent() {
    return whatEvent;
  }

  public void setWhatEvent(String whatEvent) {
    this.whatEvent = whatEvent;
  }
}
