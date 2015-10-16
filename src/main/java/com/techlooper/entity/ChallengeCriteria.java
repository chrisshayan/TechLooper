package com.techlooper.entity;

import java.io.Serializable;

/**
 * Created by phuonghqh on 10/16/15.
 */
public class ChallengeCriteria implements Serializable {

  private String name;

  private Long weight;

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
}
