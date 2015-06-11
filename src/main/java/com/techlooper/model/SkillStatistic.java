package com.techlooper.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class SkillStatistic {

    private String skillName;
    private String logoUrl;
    private String webSite;
    private List<SkillLink> usefulLinks;
    private Long totalJob;

    public SkillStatistic(){}

    public SkillStatistic(String skillName, Long totalJob) {
        this.totalJob = totalJob;
        this.skillName = skillName;
    }

    private List<Histogram> histograms = new ArrayList<>();

    public List<Histogram> getHistograms() {
        return histograms;
    }

    public void setHistograms(List<Histogram> histograms) {
        this.histograms = histograms;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public List<SkillLink> getUsefulLinks() {
        return usefulLinks;
    }

    public void setUsefulLinks(List<SkillLink> usefulLinks) {
        this.usefulLinks = usefulLinks;
    }

    public Long getTotalJob() {
        return totalJob;
    }

    public void setTotalJob(Long totalJob) {
        this.totalJob = totalJob;
    }

    public static class Builder {

        private SkillStatistic instance = new SkillStatistic();

        public Builder withHistogram(Histogram histogram) {
            instance.histograms.add(histogram);
            return this;
        }

        public Builder withSkillName(String skillName) {
            instance.skillName = skillName;
            return this;
        }

        public Builder withLogoUrl(String logoUrl) {
            instance.logoUrl = logoUrl;
            return this;
        }

        public Builder withWebSite(String webSite) {
            instance.webSite = webSite;
            return this;
        }

        public Builder withUsefulLinks(List<SkillLink> usefulLinks) {
            instance.usefulLinks = usefulLinks;
            return this;
        }

        public SkillStatistic build() {
            return instance;
        }
    }
}
