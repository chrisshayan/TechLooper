package com.techlooper.model;

/**
 * Created by phuonghqh on 12/11/14.
 */
public class SocialResponse {

    private String token = "";

    private String key = "";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class Builder {
        private SocialResponse instance = new SocialResponse();

        public static Builder get() {
            return new Builder();
        }

        public Builder withToken(String token) {
            instance.token = token;
            return this;
        }

        public Builder withKey(String key) {
            instance.key = key;
            return this;
        }

        public SocialResponse build() {
            return instance;
        }
    }
}
