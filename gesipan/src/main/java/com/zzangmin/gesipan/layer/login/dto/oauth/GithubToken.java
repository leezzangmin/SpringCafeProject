package com.zzangmin.gesipan.layer.login.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class GithubToken {

    @JsonProperty("access_token")
    private final String accessToken;
    private final String scope;
    @JsonProperty("token_type")
    private final String tokenType;

    public String getTokenHeaderString() {
        return "token " + this.accessToken;
    }

}

