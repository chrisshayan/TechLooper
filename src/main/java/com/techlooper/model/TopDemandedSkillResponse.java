package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 6/12/15.
 */
public class TopDemandedSkillResponse {

    private String skillName;

    private long count;

    public TopDemandedSkillResponse() {}

    public TopDemandedSkillResponse(String skillName, long count) {
        this.skillName = skillName;
        this.count = count;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
