package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/21/14.
 */
public class TechnicalTerm {

    private String key;
    private List<String> searchTexts;
    private String label;
    private String logoUrl;
    private String webSite;
    private List<SkillLink> usefulLinks;
    private List<Skill> skills;

    public TechnicalTerm() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getSearchTexts() {
        return searchTexts;
    }

    public void setSearchTexts(List<String> searchTexts) {
        this.searchTexts = searchTexts;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public String toString() {
        return this.key;
    }
}
