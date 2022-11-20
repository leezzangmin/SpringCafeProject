package com.zzangmin.gesipan.component.login.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.component.login.dto.oauth.GithubToken;
import com.zzangmin.gesipan.component.login.dto.oauth.UserResources;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.service.JwtProvider;
import com.zzangmin.gesipan.component.login.service.GithubOauthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@AllArgsConstructor
@RestController
public class LoginController {
    private final static String githubOauthURL = "https://github.com/login/oauth/authorize?client_id=" + System.getenv("GITHUB_CLIENT_ID") + "&scope=user";
    private final GithubOauthService githubOauthService;
    private final JwtProvider jwtProvider;

    @GetMapping("/oauth/github-url")
    public ResponseEntity<String> githubOauthURL() {
        return ResponseEntity.ok(githubOauthURL);
    }


    @GetMapping("/login")
    public void callbackAndLoginProcess(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        GithubToken githubAccessToken = githubOauthService.getAccessToken(code);
        UserResources userResources = githubOauthService.getUserResources(githubAccessToken);
        Users user = githubOauthService.upsert(userResources);

        String jwt = jwtProvider.createToken(user.getUserId());
        Cookie cookie = new Cookie("X-AUTH-TOKEN", jwt);
        response.addCookie(cookie);
    }

}
