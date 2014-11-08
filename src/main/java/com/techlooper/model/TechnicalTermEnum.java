package com.techlooper.model;

import com.techlooper.util.LookUpUtils;

/**
 * Created by chrisshayan on 7/14/14.
 * This file is temporary, till we finish the MVP. As soon as we managed to find out this idea is a working business model, we have to fetch this skills automatically.
 */
public enum TechnicalTermEnum {
  /**
   * QA refers to the job which is hiring Quality Assurance
   */
  QA("qa"),

  /**
   * BA refers to the job which is hiring business analysts
   */
  BA("ba"),

  /**
   * DBA refers to the job which is hiring database administrators
   */
  DBA("dba"),

  /**
   * PROJECT_MANAGER refers to the job which is hiring Project Managers
   */
  PROJECT_MANAGER("project manager"),

  /**
   * PYTHON refers to the job which is hiring Python developers
   */
  PYTHON("python"),

  /**
   * RUBY refers to the job which is hiring RUBY developers
   */
  RUBY("ruby"),

  /**
   * PHP refers to the job which is hiring PHP developers
   */
  PHP("php"),

  /**
   * JAVA refers to the job which is hiring Java developers
   */
  JAVA("java"),

  /**
   * DOTNET refers to the job which is hiring .net developers
   */
  DOTNET(".net");

  /**
   * Default is empty technical term
   */
//  EMPTY("");

  private String value;

  TechnicalTermEnum(String value) {
    this.value = value;
  }

  public static TechnicalTermEnum lookUp(String value) {
    return LookUpUtils.lookup(TechnicalTermEnum.class, value, null);
  }

  public String value() {
    return value;
  }

  public String toString() {
    return value;
  }
}
