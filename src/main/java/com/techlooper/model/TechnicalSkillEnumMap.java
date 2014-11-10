package com.techlooper.model;

import java.util.EnumMap;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class TechnicalSkillEnumMap {

    private EnumMap<TechnicalTermEnum, List<String>> technicalSkill =
            new EnumMap<>(TechnicalTermEnum.class);

    public List<String> skillOf(TechnicalTermEnum term) {
        return technicalSkill.get(term);
    }

    public void put(TechnicalTermEnum key, List<String> skills) {
        technicalSkill.put(key, skills);
    }

    public boolean containsKey(Object key) {
        return technicalSkill.containsKey(key);
    }

}
