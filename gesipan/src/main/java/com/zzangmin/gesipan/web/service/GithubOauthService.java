package com.zzangmin.gesipan.web.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzangmin.gesipan.web.dto.oauth.GithubToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class GithubOauthService {

    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String GET_RESOURCE_URL = "https://api.github.com/user/emails";
//    @Value("${github.client.id}")
    private static final String clientId = System.getenv("GITHUB_CLIENT_SECRET");
//    @Value("${github.secret}")
    private static final String secret = System.getenv("GITHUB_CLIENT_SECRET");



    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    public GithubToken getAccessToken(String code) {
        Map<String, String> requestBody = generateRequestBody(code);
        log.info("requestbody :",String.valueOf(requestBody));
        return restTemplate.postForObject(ACCESS_TOKEN_URL, requestBody, GithubToken.class);
    }

    public Optional<String> getUserEmail(GithubToken token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        headers.set("Authorization", token.getTokenHeaderString());
        ResponseEntity<String> userResource = restTemplate.exchange(GET_RESOURCE_URL, HttpMethod.GET, request, String.class);
        JsonNode jsonNode = objectMapper.readTree(userResource.getBody());
        for (JsonNode node : jsonNode) {
            if (node.get("primary").asBoolean()) {
                log.info("email :", node.get("email").textValue());
                return Optional.of(node.get("email").textValue());
            }
        }
        return Optional.of(null);
    }

    private Map<String,String> generateRequestBody(String code) {
        return Map.of("client_id", clientId,
                "client_secret", secret,
                "code", code);
    }

}
