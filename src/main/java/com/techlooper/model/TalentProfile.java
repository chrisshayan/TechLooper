package com.techlooper.model;

import com.techlooper.entity.userimport.UserImportEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by NguyenDangKhoa on 3/23/15.
 */
public class TalentProfile {

    private UserImportEntity userImportEntity;

    private Map<String,Long> skillMap;

    public UserImportEntity getUserImportEntity() {
        return userImportEntity;
    }

    public void setUserImportEntity(UserImportEntity userImportEntity) {
        this.userImportEntity = userImportEntity;
    }

    public Map<String, Long> getSkillMap() {
        return skillMap;
    }

    public void setSkillMap(Map<String, Long> skillMap) {
        this.skillMap = skillMap;
    }

    public static class Builder {

        private TalentProfile instance = new TalentProfile();

        public Builder withUserImportEntity(UserImportEntity userImportEntity) {
            instance.userImportEntity = userImportEntity;
            return this;
        }

        public Builder withSkillMap(Map<String,Long> skillMap) {
            instance.skillMap = skillMap;
            return this;
        }

        public TalentProfile build() {
            return instance;
        }
    }
}
