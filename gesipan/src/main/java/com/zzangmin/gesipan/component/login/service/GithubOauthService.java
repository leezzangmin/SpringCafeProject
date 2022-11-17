package com.zzangmin.gesipan.component.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.component.login.dto.oauth.GithubToken;
import com.zzangmin.gesipan.component.login.dto.oauth.UserResources;
import com.zzangmin.gesipan.component.login.entity.Users;

public interface GithubOauthService {

    GithubToken getAccessToken(String code);
    UserResources getUserResources(GithubToken token) throws JsonProcessingException ;
    Users upsert(UserResources userResources);


}
