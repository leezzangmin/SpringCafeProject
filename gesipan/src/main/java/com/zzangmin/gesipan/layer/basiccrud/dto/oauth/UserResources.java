package com.zzangmin.gesipan.layer.basiccrud.dto.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)//https://coding-today.tistory.com/24 [오늘도 코딩:티스토리]
public class UserResources {
    @JsonProperty("email")
    private String userEmail;
    @JsonProperty("name")
    private String userName;
    @JsonProperty("login")
    private String userNickname;

}
