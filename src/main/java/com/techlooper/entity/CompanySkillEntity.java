package com.techlooper.entity;

/**
 * Created by phuonghqh on 4/2/15.
 */
public class CompanySkillEntity {

  private Long skillId;

  private String skillName;

  private Long skillWeight;

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

  public Long getSkillWeight() {
    return skillWeight;
  }

  public void setSkillWeight(Long skillWeight) {
    this.skillWeight = skillWeight;
  }
}
