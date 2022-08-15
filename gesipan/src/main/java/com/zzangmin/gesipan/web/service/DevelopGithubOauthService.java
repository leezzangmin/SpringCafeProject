package com.zzangmin.gesipan.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.web.dto.oauth.GithubToken;
import com.zzangmin.gesipan.web.dto.oauth.UserResources;
import com.zzangmin.gesipan.web.entity.Users;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"local", "test"})
public class DevelopGithubOauthService implements GithubOauthService {
    @Override
    public GithubToken getAccessToken(String code) {
        return null;
    }

    @Override
    public UserResources getUserResources(GithubToken token) throws JsonProcessingException {
        return null;
    }

    @Override
    public Users upsert(UserResources userResources) {
        return null;
    }
}
