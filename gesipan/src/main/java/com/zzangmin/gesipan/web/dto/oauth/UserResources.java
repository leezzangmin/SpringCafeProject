package com.zzangmin.gesipan.web.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserResources {
    @JsonProperty("email")
    private String userEmail;
    @JsonProperty("name")
    private String userName;
    @JsonProperty("login")
    private String userNickname;

}
