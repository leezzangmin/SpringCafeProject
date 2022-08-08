package com.zzangmin.gesipan.web.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzangmin.gesipan.dao.UsersRepository;
import com.zzangmin.gesipan.web.dto.oauth.GithubToken;
import com.zzangmin.gesipan.web.dto.oauth.UserResources;
import com.zzangmin.gesipan.web.entity.UserRole;
import com.zzangmin.gesipan.web.entity.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
public class GithubOauthService {
    private final UsersRepository usersRepository;

    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GET_RESOURCE_URL = "https://api.github.com/";
    private static final String clientId = System.getenv("GITHUB_CLIENT_ID");
    private static final String secret = System.getenv("GITHUB_CLIENT_SECRET");

    private final ObjectMapper objectMapper;


    public GithubToken getAccessToken(String code) {
        Map<String, String> requestBody = generateRequestBodyForAccessToken(code);
        log.info("requestBody: {}", requestBody);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(ACCESS_TOKEN_URL, requestBody, GithubToken.class);
    }

    public UserResources getUserResources(GithubToken token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token.getTokenHeaderString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String userResource = restTemplate.exchange(GET_RESOURCE_URL + token.getScope(), HttpMethod.GET, request, String.class).getBody();
        UserResources userResources = objectMapper.readValue(userResource, UserResources.class);

        log.info("userResources: {}", userResources);
        return userResources;
    }

    /**
     * 이미 가입했으면 update, 신규회원이면 insert
     * @param userResources
     */
    @Transactional
    public void upsert(UserResources userResources) {
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
        log.info("user: {}", user);
    }

    private Map<String,String> generateRequestBodyForAccessToken(String code) {
        return Map.of("client_id", clientId,
                "client_secret", secret,
                "code", code);
    }

}
