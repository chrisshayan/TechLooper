package com.techlooper.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by phuonghqh on 8/18/15.
 */
public class WebinarInfoDto implements Serializable {

  private String name;

  private String stateDate;

  private String endDate;

  private String description;

  private Set<String> attendees;

  private String organiser;

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
