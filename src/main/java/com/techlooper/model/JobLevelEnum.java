package com.techlooper.model;

/**
 * Created by phuonghqh on 5/5/15.
 */
public enum JobLevelEnum {
  ENTRY(1), EXPERIENCED(5, 6), MANAGER(7), DIRECTOR_PLUS(10, 3, 4, 8, 9);

  private int[] ids;

  private JobLevelEnum(int... ids) {
    this.ids = ids;
  }

  public int[] getIds() {
    return ids;
  }
}
