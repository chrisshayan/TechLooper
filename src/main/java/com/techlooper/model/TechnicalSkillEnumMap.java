package com.techlooper.model;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 11/4/14.
 */
public class TechnicalSkillEnumMap {

    private static EnumMap<TechnicalTermEnum, List<String>> technicalSkill;

    static {
        technicalSkill = new EnumMap<TechnicalTermEnum, List<String>>(TechnicalTermEnum.class);
        technicalSkill.put(TechnicalTermEnum.JAVA, Arrays.asList("Spring", "JUnit", "Maven", "Hibernate", "EJB", "JSF", "Tomcat", "Jenkins", "Struts", "XML"));
        technicalSkill.put(TechnicalTermEnum.DOTNET, Arrays.asList("ASP.NET MVC", "SQL", "AngularJS", "C#", "JQuery", "VB.NET", "HTML", "Javascript", "WCF", "LINQ"));
        technicalSkill.put(TechnicalTermEnum.PHP, Arrays.asList("Symfony", "mysql", "Code Igniter", "Cakephp", "Zend", "Drupal", "HTML", "JQuery", "AJAX", "Linux"));
    }

    public static List<String> skillOf(TechnicalTermEnum term) {
        return technicalSkill.get(term);
    }

    public static boolean containsKey(Object key) {
        return technicalSkill.containsKey(key);
    }

}
