package com.techlooper.model;

import java.util.List;

/**
 * Created by phuonghqh on 4/17/15.
 */
public class TermStatisticRequest {

    private String term;

    private List<String> skills;

    private List<Integer> jobLevelIds;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<Integer> getJobLevelIds() {
        return jobLevelIds;
    }

    public void setJobLevelIds(List<Integer> jobLevelIds) {
        this.jobLevelIds = jobLevelIds;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
