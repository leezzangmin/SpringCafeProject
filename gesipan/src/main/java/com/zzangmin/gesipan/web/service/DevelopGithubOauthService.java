package com.zzangmin.gesipan.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.dao.UsersRepository;
import com.zzangmin.gesipan.web.dto.oauth.GithubToken;
import com.zzangmin.gesipan.web.dto.oauth.UserResources;
import com.zzangmin.gesipan.web.entity.UserRole;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;


/**
 *  성능 테스트용 DevelopGithubOauthService
 *  github api 호출하는 부분을 제외하고 기존 로직과 동일하게 구현하였음.
 */
@RequiredArgsConstructor
@Service
@Profile({"local", "test"})
public class DevelopGithubOauthService implements GithubOauthService {

    private final UsersRepository usersRepository;
    private static final String clientId = System.getenv("GITHUB_CLIENT_ID");
    private static final String secret = System.getenv("GITHUB_CLIENT_SECRET");

    @Override
    public GithubToken getAccessToken(String code) {
        Map<String, String> requestBody = generateRequestBodyForAccessToken(code);
        RestTemplate restTemplate = new RestTemplate();
        return new GithubToken("fake_access_code", "fake_scope", "fake_token_type");
    }

    @Override
    public UserResources getUserResources(GithubToken token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token.getTokenHeaderString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String userResource = "fakeUserResource";
        return new UserResources("fakeEmail@gmail.com", "fake_name", "fake_nickname");

    }
    @Transactional
    @Override
    public Users upsert(UserResources userResources) {
        LocalDateTime now = LocalDateTime.now();
        Users user = usersRepository.findByEmail(userResources.getUserEmail())
                .orElseGet(() -> usersRepository.save(
                        Users.builder()
                                .userEmail(userResources.getUserEmail())
                                .userName(userResources.getUserName())
                                .userNickname(userResources.getUserNickname())
                                .userRole(UserRole.일반)
                                .createdAt(now)
                                .updatedAt(now)
                                .build()));
        if (!user.getUserName().equals(userResources.getUserName()) || !user.getUserNickname().equals(userResources.getUserNickname())) {
            user.update(userResources.getUserName(), userResources.getUserNickname(), now);
        }
        return user;
    }

    private Map<String,String> generateRequestBodyForAccessToken(String code) {
        return Map.of("client_id", clientId,
                "client_secret", secret,
                "code", code);
    }

}