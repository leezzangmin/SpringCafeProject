package com.zzangmin.gesipan.layer.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.layer.login.dto.oauth.GithubToken;
import com.zzangmin.gesipan.layer.login.dto.oauth.UserResources;
import com.zzangmin.gesipan.layer.login.entity.Users;

public interface GithubOauthService {

    GithubToken getAccessToken(String code);
    UserResources getUserResources(GithubToken token) throws JsonProcessingException ;
    Users upsert(UserResources userResources);


}
