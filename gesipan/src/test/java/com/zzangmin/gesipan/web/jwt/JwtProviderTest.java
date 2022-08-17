package com.zzangmin.gesipan.web.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("createToken으로 만든 토큰이 resolve 되었을 때 동일해야 한다.")
    void createToken() {
        String givenToken = jwtProvider.createToken("짱짱민");
        Cookie cookie = new Cookie("X-AUTH-TOKEN", givenToken);
        Cookie[] cookies = new Cookie[]{cookie};

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(cookies);
        Optional<String> s = jwtProvider.resolveToken(request);
        String getToken = s.get();

        Assertions.assertThat(givenToken).isEqualTo(getToken);
    }

    @Test
    @DisplayName("미리 넣어둔 유저 정보와 token에서 뽑은 정보가 일치해야한다.")
    void getUserInfo() {
        //given
        String givenToken = jwtProvider.createToken("짱짱민");
        //when
        String userInfo = jwtProvider.getUserInfo(givenToken);
        //then
        Assertions.assertThat("짱짱민").isEqualTo(userInfo);
    }

    @Test
    @DisplayName("유효한 토큰을 넘겨주면 true를, 아니면 false를 반환해야한다")
    void isValidToken() {
        //given
        String jwt = jwtProvider.createToken("ckdals");
        String invalidJWT = "it's invalid token";
        //when
        boolean validTokenFlag = jwtProvider.isValidToken(jwt);
        boolean invalidTokenFlag = jwtProvider.isValidToken(invalidJWT);
        //then
        Assertions.assertThat(validTokenFlag).isTrue();
        Assertions.assertThat(invalidTokenFlag).isFalse();
    }
}