package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 3/11/15.
 */
public class Talent {

    private String email;

    private String imageUrl;

    private String username;

    private String fullName;

    private String description;

    private String jobTitle;

    private String company;

    private String location;

    private List<String> skills;

    private Long score;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
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

        Talent talent = (Talent) o;

        if (!email.equals(talent.email)) return false;

        return true;
    }

    public int hashCode() {
        return email.hashCode();
    }

    public static class Builder {

        private Talent instance = new Talent();

        public Builder withEmail(String email) {
            instance.email = email;
            return this;
        }

        public Builder withImageUrl(String imageUrl) {
            instance.imageUrl = imageUrl;
            return this;
        }

        public Builder withUsername(String username) {
            instance.username = username;
            return this;
        }

        public Builder withFullName(String fullName) {
            instance.fullName = fullName;
            return this;
        }

        public Builder withDescription(String description) {
            instance.description = description;
            return this;
        }

        public Builder withJobTitle(String jobTitle) {
            instance.jobTitle = jobTitle;
            return this;
        }

        public Builder withCompany(String company) {
            instance.company = company;
            return this;
        }

        public Builder withLocation(String location) {
            instance.location = location;
            return this;
        }

        public Builder withSkills(List<String> skills) {
            instance.skills = skills;
            return this;
        }

        public Builder withScore(Long score) {
            instance.score = score;
            return this;
        }

        public Talent build() {
            return instance;
        }
    }
}
