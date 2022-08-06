package com.zzangmin.gesipan.web.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubToken {

    @JsonProperty("access_token")
    private String accessToken;
    private String scope;
    @JsonProperty("token_type")
    private String tokenType;

    public GithubToken() {
    }

    public GithubToken(String accessToken, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getTokenHeaderString() {
        return this.tokenType + " " + this.accessToken;
    }

}

