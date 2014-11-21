package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/21/14.
 */
public class Skill {

    private String name;
    private String logoUrl;
    private String webSite;
    private List<SkillLink> usefulLinks;

    public Skill() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
