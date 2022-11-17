package com.zzangmin.gesipan.component.login.dto.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResources {
    @JsonProperty("email")
    private final String userEmail;
    @JsonProperty("name")
    private final String userName;
    @JsonProperty("login")
    private final String userNickname;

}
