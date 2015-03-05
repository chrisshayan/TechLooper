package com.techlooper.entity;

/**
 * Created by phuonghqh on 12/12/14.
 */
public class TwitterAccount {

    private String providerAccountId;

    private String providerAccountName;

    public String getProviderAccountId() {
        return providerAccountId;
    }

    public void setProviderAccountId(String providerAccountId) {
        this.providerAccountId = providerAccountId;
    }

    public String getProviderAccountName() {
        return providerAccountName;
    }

    public void setProviderAccountName(String providerAccountName) {
        this.providerAccountName = providerAccountName;
    }
}
