package com.zzangmin.gesipan.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.web.dto.oauth.GithubToken;
import com.zzangmin.gesipan.web.dto.oauth.UserResources;
import com.zzangmin.gesipan.web.entity.Users;

public interface GithubOauthService {

    GithubToken getAccessToken(String code);
    UserResources getUserResources(GithubToken token) throws JsonProcessingException ;
    Users upsert(UserResources userResources);


}
