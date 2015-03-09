package com.techlooper.entity;


import java.util.List;

/**
 * Created by phuonghqh on 12/15/14.
 */
public class EducationEntry {
    private Reference school;
    private Reference year;
    private List<Reference> concentration;
    private String type;

    public Reference getSchool() {
        return school;
    }

    public void setSchool(Reference school) {
        this.school = school;
    }

    public Reference getYear() {
        return year;
    }

    public void setYear(Reference year) {
        this.year = year;
    }

    public List<Reference> getConcentration() {
        return concentration;
    }

    public void setConcentration(List<Reference> concentration) {
        this.concentration = concentration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
