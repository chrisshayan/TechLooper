package com.techlooper.dto;

import com.techlooper.model.ChallengeDetailDto;
import com.techlooper.model.ProjectDto;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by phuonghqh on 8/11/15.
 */
public class DashBoardInfo implements Serializable {

  Collection<ChallengeDetailDto> challenges;

  Collection<ProjectDto> projects;

  public Collection<ChallengeDetailDto> getChallenges() {
    return challenges;
  }

  public void setChallenges(Collection<ChallengeDetailDto> challenges) {
    this.challenges = challenges;
  }

  public Collection<ProjectDto> getProjects() {
    return projects;
  }

  public void setProjects(Collection<ProjectDto> projects) {
    this.projects = projects;
  }

  public static class DashBoardInfoBuilder {
    private DashBoardInfo dashBoardInfo;

    private DashBoardInfoBuilder() {
      dashBoardInfo = new DashBoardInfo();
    }

    public DashBoardInfoBuilder withChallenges(Collection<ChallengeDetailDto> challenges) {
      dashBoardInfo.challenges = challenges;
      return this;
    }

    public DashBoardInfoBuilder withProjects(Collection<ProjectDto> projects) {
      dashBoardInfo.projects = projects;
      return this;
    }

    public static DashBoardInfoBuilder dashBoardInfo() {
      return new DashBoardInfoBuilder();
    }

    public DashBoardInfo build() {
      return dashBoardInfo;
    }
  }
}
