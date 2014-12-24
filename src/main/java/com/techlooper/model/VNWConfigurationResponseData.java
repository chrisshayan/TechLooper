package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VNWConfigurationResponseData {

  @JsonProperty(value = "locations")
  private List<ConfigurationLocation> locations;

  @JsonProperty(value = "categories")
  private List<ConfigurationCategory> categories;

  @JsonProperty(value = "job_levels")
  private List<ConfigurationJobLevel> levels;

  @JsonProperty(value = "degree")
  private List<ConfigurationDegree> degrees;

  public List<ConfigurationLocation> getLocations() {
    return locations;
  }

  public void setLocations(List<ConfigurationLocation> locations) {
    this.locations = locations;
  }

  public List<ConfigurationCategory> getCategories() {
    return categories;
  }

  public void setCategories(List<ConfigurationCategory> categories) {
    this.categories = categories;
  }

  public List<ConfigurationJobLevel> getLevels() {
    return levels;
  }

  public void setLevels(List<ConfigurationJobLevel> levels) {
    this.levels = levels;
  }

  public List<ConfigurationDegree> getDegrees() {
    return degrees;
  }

  public void setDegrees(List<ConfigurationDegree> degrees) {
    this.degrees = degrees;
  }

  public static class ConfigurationItem {
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConfigurationLocation extends ConfigurationItem {

    @JsonProperty(value = "location_id")
    private String locationId;

    @JsonProperty(value = "lang_en")
    private String english;

    @JsonProperty(value = "lang_vn")
    private String vietnamese;

    public String getLocationId() {
      return locationId;
    }

    public void setLocationId(String locationId) {
      this.locationId = locationId;
    }

    public String getEnglish() {
      return english;
    }

    public void setEnglish(String english) {
      this.english = english;
    }

    public String getVietnamese() {
      return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
      this.vietnamese = vietnamese;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConfigurationCategory {

    @JsonProperty(value = "category_id")
    private String categoryId;

    @JsonProperty(value = "lang_en")
    private String english;

    @JsonProperty(value = "lang_vn")
    private String vietnamese;

    public String getCategoryId() {
      return categoryId;
    }

    public void setCategoryId(String categoryId) {
      this.categoryId = categoryId;
    }

    public String getEnglish() {
      return english;
    }

    public void setEnglish(String english) {
      this.english = english;
    }

    public String getVietnamese() {
      return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
      this.vietnamese = vietnamese;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConfigurationJobLevel {

    @JsonProperty(value = "location_id")
    private String locationId;

    @JsonProperty(value = "lang_en")
    private String english;

    @JsonProperty(value = "lang_vn")
    private String vietnamese;

    public String getLocationId() {
      return locationId;
    }

    public void setLocationId(String locationId) {
      this.locationId = locationId;
    }

    public String getEnglish() {
      return english;
    }

    public void setEnglish(String english) {
      this.english = english;
    }

    public String getVietnamese() {
      return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
      this.vietnamese = vietnamese;
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConfigurationDegree extends ConfigurationItem {

    @JsonProperty(value = "degree_id")
    private String degreeId;

    @JsonProperty(value = "lang_en")
    private String english;

    @JsonProperty(value = "lang_vn")
    private String vietnamese;

    public String getDegreeId() {
      return degreeId;
    }

    public void setDegreeId(String degreeId) {
      this.degreeId = degreeId;
    }

    public String getEnglish() {
      return english;
    }

    public void setEnglish(String english) {
      this.english = english;
    }

    public String getVietnamese() {
      return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
      this.vietnamese = vietnamese;
    }
  }
}
