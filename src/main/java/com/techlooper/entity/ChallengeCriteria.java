package com.techlooper.entity;

import com.techlooper.util.DataUtils;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import java.io.Serializable;

import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by phuonghqh on 10/16/15.
 */
public class ChallengeCriteria implements Serializable {

  @Field(type = String, index = FieldIndex.not_analyzed)
  private String criteriaId = DataUtils.generateStringId();

  private String name;

  private Long weight;

  public java.lang.String getCriteriaId() {
    return criteriaId;
  }

  public void setCriteriaId(java.lang.String criteriaId) {
    this.criteriaId = criteriaId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getWeight() {
    return weight;
  }

  public void setWeight(Long weight) {
    this.weight = weight;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ChallengeCriteria that = (ChallengeCriteria) o;

    return !(criteriaId != null ? !criteriaId.equals(that.criteriaId) : that.criteriaId != null);

  }

  public int hashCode() {
    return criteriaId != null ? criteriaId.hashCode() : 0;
  }

  public static class ChallengeCriteriaBuilder {
    private ChallengeCriteria challengeCriteria;

    private ChallengeCriteriaBuilder() {
      challengeCriteria = new ChallengeCriteria();
    }

    public ChallengeCriteriaBuilder withCriteriaId(String criteriaId) {
      challengeCriteria.criteriaId = criteriaId;
      return this;
    }

    public ChallengeCriteriaBuilder withName(String name) {
      challengeCriteria.name = name;
      return this;
    }

    public ChallengeCriteriaBuilder withWeight(Long weight) {
      challengeCriteria.weight = weight;
      return this;
    }

    public static ChallengeCriteriaBuilder challengeCriteria() {
      return new ChallengeCriteriaBuilder();
    }

    public ChallengeCriteria build() {
      return challengeCriteria;
    }
  }
}
