package com.techlooper.entity;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import java.io.Serializable;

import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by phuonghqh on 10/19/15.
 */
public class ChallengeRegistrantCriteria implements Serializable {

    @Field(type = String, index = FieldIndex.not_analyzed)
    private String criteriaId;

    private String name;

    private Long weight;

    private Long score;

    private String comment;

    public java.lang.String getComment() {
        return comment;
    }

    public void setComment(java.lang.String comment) {
        this.comment = comment;
    }

    public java.lang.String getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(java.lang.String criteriaId) {
        this.criteriaId = criteriaId;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChallengeRegistrantCriteria that = (ChallengeRegistrantCriteria) o;
        return !(criteriaId != null ? !criteriaId.equals(that.criteriaId) : that.criteriaId != null);
    }

    public int hashCode() {
        return criteriaId != null ? criteriaId.hashCode() : 0;
    }
}
