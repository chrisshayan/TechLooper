package com.techlooper.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Long;
import static org.springframework.data.elasticsearch.annotations.FieldType.String;

/**
 * Created by chrisshayan on 7/10/14.
 */
@Document(indexName = "vietnamworks")
public class JobEntity {

    @Id
    private String id;

    @Field(type = String)
    private String companyDesc;

    @Field(type = Long)
    private long companyId;

    @Field(type = String)
    private String companyProfile;

    @Field(type = String)
    private String jobDescription;

    @Field(type = String)
    private String jobTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that "textually represents" this
     * object. The result should be a concise but informative representation that
     * is easy for a person to read. It is recommended that all subclasses
     * override this method.
     * <p>
     * The {@code toString} method for class {@code Object} returns a string
     * consisting of the name of the class of which the object is an instance,
     * the at-sign character `{@code @}', and the unsigned hexadecimal
     * representation of the hash code of the object. In other words, this method
     * returns a string equal to the value of: <blockquote>
     * <p>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre>
     * <p>
     * </blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
