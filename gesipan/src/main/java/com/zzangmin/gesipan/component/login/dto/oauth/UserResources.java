package com.zzangmin.gesipan.component.login.dto.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zzangmin.gesipan.component.embeddable.BaseTime;
import com.zzangmin.gesipan.component.login.entity.UserRole;
import com.zzangmin.gesipan.component.login.entity.Users;
import java.time.LocalDateTime;
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

    public Users toEntity() {
        LocalDateTime now = LocalDateTime.now();
        return Users.builder()
            .userEmail(this.getUserEmail())
            .userName(this.getUserName())
            .userNickname(this.getUserNickname())
            .userRole(UserRole.NORMAL)
            .baseTime(new BaseTime(now, now))
            .build();
    }
}
