package com.zzangmin.gesipan.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@Profile({"local","test"})
public class DevelopLoginCheckInterceptor implements LoginCheckInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("DevelopLoginCheckInterceptor 인증 하이패스");
        return true;
    }

}
