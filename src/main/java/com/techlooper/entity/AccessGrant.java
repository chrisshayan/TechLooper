package com.techlooper.entity;

/**
 * Created by phuonghqh on 12/12/14.
 */
public class AccessGrant {
    private String accessToken;
    private String scope;
    private String refreshToken;
    private Long expireTime;
    private String value;
    private String secret;
    private String authorizeUrl;


    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public static class AccessGrantBuilder {
        private AccessGrant accessGrant;

        private AccessGrantBuilder() {
            accessGrant = new AccessGrant();
        }

        public static AccessGrantBuilder accessGrant() {
            return new AccessGrantBuilder();
        }

        public AccessGrantBuilder withAccessToken(String accessToken) {
            accessGrant.accessToken = accessToken;
            return this;
        }

        public AccessGrantBuilder withScope(String scope) {
            accessGrant.scope = scope;
            return this;
        }

        public AccessGrantBuilder withRefreshToken(String refreshToken) {
            accessGrant.refreshToken = refreshToken;
            return this;
        }

        public AccessGrantBuilder withExpireTime(Long expireTime) {
            accessGrant.expireTime = expireTime;
            return this;
        }

        public AccessGrantBuilder withValue(String value) {
            accessGrant.value = value;
            return this;
        }

        public AccessGrantBuilder withSecret(String secret) {
            accessGrant.secret = secret;
            return this;
        }

        public AccessGrantBuilder withAuthorizeUrl(String authorizeUrl) {
            accessGrant.authorizeUrl = authorizeUrl;
            return this;
        }

        public AccessGrant build() {
            return accessGrant;
        }
    }
}
