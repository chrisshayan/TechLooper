package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 6/3/15.
 */
public class JobSkill {

    private Long skillId;

    private String skillName;

    private Integer skillWeight;

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getSkillWeight() {
        return skillWeight;
    }

    public void setSkillWeight(Integer skillWeight) {
        this.skillWeight = skillWeight;
    }
}
