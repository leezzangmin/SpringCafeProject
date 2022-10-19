package com.zzangmin.gesipan.layer.basiccrud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.layer.basiccrud.dto.oauth.GithubToken;
import com.zzangmin.gesipan.layer.basiccrud.dto.oauth.UserResources;
import com.zzangmin.gesipan.layer.basiccrud.entity.Users;

public interface GithubOauthService {

    GithubToken getAccessToken(String code);
    UserResources getUserResources(GithubToken token) throws JsonProcessingException ;
    Users upsert(UserResources userResources);


}
