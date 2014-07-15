package com.techlooper.model;

/**
 * Created by chrisshayan on 7/14/14.
 */
public enum TechnicalTermEnum {
    /**
     * PHP refers to the job which is hiring PHP developers
     */
    PHP("php"),


    /**
     * JAVA refers to the job which is hiring Java developers
     */
    JAVA("java"),

    /**
     * DOTNET refers to the job which is hiring .net developers
     */
    DOTNET(".net");

    private String name;

    TechnicalTermEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
