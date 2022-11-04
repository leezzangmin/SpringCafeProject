package com.zzangmin.gesipan.layer.login.interceptor;

import static org.junit.jupiter.api.Assertions.*;

import com.zzangmin.gesipan.layer.login.service.JwtProvider;
import javax.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class DeployLoginCheckInterceptorTest {

    JwtProvider jwtProvider = new JwtProvider("fake_secret");
    DeployLoginCheckInterceptor deployLoginCheckInterceptor = new DeployLoginCheckInterceptor(jwtProvider);

    @DisplayName("변조된 토큰을 HttpRequest에 쿠키로 담아 요청하면 에러를 발생시켜야 한다.")
    @Test
    void 가짜토큰에러테스트() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Cookie cookie = new Cookie("X-AUTH-TOKEN", "가짜토큰, 인증실패해야함");
        request.setCookies(cookie);
        //when then
        Assertions.assertThatThrownBy(() -> deployLoginCheckInterceptor.preHandle(request, response, null));
    }

    @DisplayName("정상 토큰을 HttpRequest에 쿠키로 담아 요청하면 true를 반환해야 한다")
    @Test
    void 정상토큰통과테스트() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String realToken = jwtProvider.createToken(1L);
        System.out.println("realToken = " + realToken);
        Cookie cookie = new Cookie("X-AUTH-TOKEN", realToken);
        request.setCookies(cookie);
        //when
        boolean returnValue = deployLoginCheckInterceptor.preHandle(request, response, null);
        //then
        Assertions.assertThat(returnValue).isTrue();
    }

}
