package com.techlooper.model;

import com.techlooper.entity.CompanyBenefit;

import java.util.List;

/**
 * Created by phuonghqh on 10/15/14.
 */
public class JobResponse {

    private String jobId;

    private String url;

    private String title;

    private String location;

    private String level;

    private String postedOn;

    private String company;

    private String videoUrl;

    private String logoUrl;

    private String salary;

    private Long salaryMin;

    private Long salaryMax;

    private Boolean topPriority;

    private List<CompanyBenefit> benefits;

    private List<JobSkill> skills;

//  public static Collection<Long> topPriorityJobIds;

    public static class Builder {

        private JobResponse instance = new JobResponse();

        public Builder withUrl(String url) {
            instance.url = url;
            return this;
        }

        public Builder withTitle(String title) {
            instance.title = title;
            return this;
        }

        public Builder withLocation(String location) {
            instance.location = location;
            return this;
        }

        public Builder withLevel(String level) {
            instance.level = level;
            return this;
        }

        public Builder withPostedOn(String postedOn) {
            instance.postedOn = postedOn;
            return this;
        }

        public Builder withCompany(String company) {
            instance.company = company;
            return this;
        }

        public Builder withVideoUrl(String videoUrl) {
            instance.videoUrl = videoUrl;
            return this;
        }

        public Builder withLogoUrl(String logoUrl) {
            instance.logoUrl = logoUrl;
            return this;
        }

        public Builder withSalary(String salary) {
            instance.salary = salary;
            return this;
        }

        public Builder withSalaryMin(Long salaryMin) {
            instance.salaryMin = salaryMin;
            return this;
        }

        public Builder withSalaryMax(Long salaryMax) {
            instance.salaryMax = salaryMax;
            return this;
        }

        public Builder withTopPriority(Boolean topPriority) {
            instance.setTopPriority(topPriority);
            return this;
        }

        public Builder withBenefits(List<CompanyBenefit> benefits) {
            instance.benefits = benefits;
            return this;
        }

        public Builder withSkills(List<JobSkill> skills) {
            instance.skills = skills;
            return this;
        }

        public JobResponse build() {
            return instance;
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobResponse that = (JobResponse) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public Long getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(Long salaryMin) {
        this.salaryMin = salaryMin;
    }

    public Long getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(Long salaryMax) {
        this.salaryMax = salaryMax;
    }

    public List<CompanyBenefit> getBenefits() {
        return benefits;
    }

    public void setBenefits(List<CompanyBenefit> benefits) {
        this.benefits = benefits;
    }

    public List<JobSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<JobSkill> skills) {
        this.skills = skills;
    }

    public Boolean getTopPriority() {
        return topPriority;
    }

    public void setTopPriority(Boolean topPriority) {
        this.topPriority = topPriority;
    }
}
