package com.zzangmin.gesipan.component.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzangmin.gesipan.component.embeddable.BaseTime;
import com.zzangmin.gesipan.component.login.repository.UsersRepository;
import com.zzangmin.gesipan.component.login.dto.oauth.GithubToken;
import com.zzangmin.gesipan.component.login.dto.oauth.UserResources;
import com.zzangmin.gesipan.component.login.entity.UserRole;
import com.zzangmin.gesipan.component.login.entity.Users;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Service
@Profile("deploy")
public class DeployGithubOauthService implements GithubOauthService {
    private final UsersRepository usersRepository;
    private final RestTemplate restTemplate;

    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GET_RESOURCE_URL = "https://api.github.com/";
    @Value("${GITHUB_CLIENT_ID}")
    private static String clientId;
    @Value("${GITHUB_CLIENT_SECRET}")
    private static String secret;


    @Override
    public GithubToken getAccessToken(String code) {
        Map<String, String> requestBody = generateRequestBodyForAccessToken(code);
        log.info("requestBody: {}", requestBody);
        return restTemplate.postForObject(ACCESS_TOKEN_URL, requestBody, GithubToken.class);
    }

    @Override
    public UserResources getUserResources(GithubToken token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token.getTokenHeaderString());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String userResource = restTemplate.exchange(GET_RESOURCE_URL + token.getScope(), HttpMethod.GET, request, String.class).getBody();
        log.info("userResource: {}", userResource);
        UserResources userResources = new ObjectMapper().readValue(userResource, UserResources.class);
        log.info("userResources: {}", userResources);
        return userResources;
    }

    /**
     * 이미 가입했으면 update, 신규회원이면 insert
     * @param userResources
     */
    @Override
    @Transactional
    public Users upsert(UserResources userResources) {
        LocalDateTime now = LocalDateTime.now();
        Users user = usersRepository.findByEmail(userResources.getUserEmail())
                        .orElseGet(() -> usersRepository.save(userResources.toEntity()));
        if (!user.getUserName().equals(userResources.getUserName()) || !user.getUserNickname().equals(userResources.getUserNickname())) {
            user.update(userResources.getUserName(), userResources.getUserNickname(), now);
        }
        log.info("user: {}", user);
        return user;
    }

    private Map<String,String> generateRequestBodyForAccessToken(String code) {
        return Map.of("client_id", clientId,
                "client_secret", secret,
                "code", code);
    }

}
