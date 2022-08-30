package com.zzangmin.gesipan.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zzangmin.gesipan.dao.UsersRepository;
import com.zzangmin.gesipan.web.dto.oauth.GithubToken;
import com.zzangmin.gesipan.web.dto.oauth.UserResources;
import com.zzangmin.gesipan.web.entity.UserRole;
import com.zzangmin.gesipan.web.entity.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeployGithubOauthServiceTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DeployGithubOauthService deployGithubOauthService;

    @Test
    void getAccessToken() {
        //given
        GithubToken githubToken = new GithubToken("fake_access_token","fake_scope","fake_type");
        given(restTemplate.postForObject(any(String.class), any(Object.class), any())).willReturn(githubToken);
        //when
        GithubToken accessToken = deployGithubOauthService.getAccessToken("fake request");
        //then
        assertThat(githubToken.getTokenHeaderString()).isEqualTo(accessToken.getTokenHeaderString());
    }

    @Test
    void getUserResources() throws JsonProcessingException {
        //given
        GithubToken githubToken = new GithubToken("fake_access_token","fake_scope","fake_type");
        ResponseEntity<String> userResourceEntity
                = ResponseEntity.of(Optional.of("{\"login\":\"leezzangmin\",\"id\":64303390,\"node_id\":\"MDQ6VXNlcjY0MzAzMzkw\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/64303390?v=4\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/leezzangmin\",\"html_url\":\"https://github.com/leezzangmin\",\"followers_url\":\"https://api.github.com/users/leezzangmin/followers\",\"following_url\":\"https://api.github.com/users/leezzangmin/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/leezzangmin/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/leezzangmin/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/leezzangmin/subscriptions\",\"organizations_url\":\"https://api.github.com/users/leezzangmin/orgs\",\"repos_url\":\"https://api.github.com/users/leezzangmin/repos\",\"events_url\":\"https://api.github.com/users/leezzangmin/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/leezzangmin/received_events\",\"type\":\"User\",\"site_admin\":false,\"name\":\"이창민\",\"company\":null,\"blog\":\"https://leezzangmin.tistory.com/\",\"location\":null,\"email\":\"ckdals12345678@gmail.com\",\"hireable\":null,\"bio\":\"말하는 감자입니다\",\"twitter_username\":null,\"public_repos\":17,\"public_gists\":0,\"followers\":15,\"following\":1,\"created_at\":\"2020-04-25T07:42:00Z\",\"updated_at\":\"2022-08-08T06:54:56Z\",\"private_gists\":9,\"total_private_repos\":4,\"owned_private_repos\":4,\"disk_usage\":85797,\"collaborators\":0,\"two_factor_authentication\":false,\"plan\":{\"name\":\"free\",\"space\":976562499,\"collaborators\":0,\"private_repos\":10000}}"));
        given(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)))
                .willReturn(userResourceEntity);
        //when
        UserResources userResources = deployGithubOauthService.getUserResources(githubToken);
        //then
        assertThat(userResources.getUserEmail()).isEqualTo("ckdals12345678@gmail.com");
        assertThat(userResources.getUserNickname()).isEqualTo("leezzangmin");
        assertThat(userResources.getUserName()).isEqualTo("이창민");
    }

    @Test
    void upsert() {
        //given
        UserResources userResources = new UserResources("ckdals123@naver.com", "이창민", "leezzangmin");
        LocalDateTime now = LocalDateTime.now();
        Users user = Users.builder()
                .userEmail("ckdals123@naver.com")
                .userName("이창민")
                .userNickname("leezzangmin")
                .userRole(UserRole.NORMAL)
                .createdAt(now)
                .updatedAt(now)
                .build();
        given(usersRepository.findByEmail(eq("ckdals123@naver.com"))).willReturn(Optional.ofNullable(user));
        //when
        Users upsertUser = deployGithubOauthService.upsert(userResources);
        //then
        assertThat(upsertUser.getUserEmail()).isEqualTo("ckdals123@naver.com");
        assertThat(upsertUser.getUserName()).isEqualTo("이창민");
        assertThat(upsertUser.getUserNickname()).isEqualTo("leezzangmin");
        assertThat(upsertUser.getCreatedAt()).isEqualTo(now);
        assertThat(upsertUser.getUpdatedAt()).isEqualTo(now);
    }
}
