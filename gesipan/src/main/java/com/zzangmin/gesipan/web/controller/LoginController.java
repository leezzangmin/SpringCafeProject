package com.zzangmin.gesipan.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.web.dto.oauth.GithubToken;
import com.zzangmin.gesipan.web.dto.oauth.UserResources;
import com.zzangmin.gesipan.web.service.GithubOauthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/** TODO: 1. code 요청하는 URL 돌려주는 api
 *        2. callback+code로 요청하면
 *          2 - 1 : access_token 발급 및 어딘가에 저장 (post요청)
 *          2 - 2 : access_token 으로 깃허브에서 resource 받아옴 (get요청) // 이름, 이메일, 닉네임
 *          2 - 3 : 받아온 resource(user)가 서버(DB,메모리)에 있거나 없으면 upsert(update+insert)
 *          2 - 4 : 받아온 정보로 암호화된 (user,expired) 정보를 넣은 JWT를 cookie에 담아 반환
 *
 */
@Slf4j
@AllArgsConstructor
@RestController
public class LoginController {
    private final static String githubOauthURL = "https://github.com/login/oauth/authorize?client_id=" + System.getenv("GITHUB_CLIENT_ID") + "&scope=user";
    private final GithubOauthService githubOauthService;



    // http://127.0.0.1:8080/oauth/github_url
    // https://github.com/login/oauth/authorize?client_id=2e32ee13d9ee74d112ec&scope=user
    @GetMapping("/oauth/github_url")
    public ResponseEntity<String> githubOauthURL() {
        return ResponseEntity.ok(githubOauthURL);
    }

    @GetMapping("/oauth/callback")
    public void callback(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        GithubToken token = githubOauthService.getAccessToken(code);
        UserResources userResources = githubOauthService.getUserResources(token);
        githubOauthService.upsert(userResources);

        Cookie cookie = new Cookie("asdf", "JWT 구현하기~");
        response.addCookie(cookie);
    }

}
