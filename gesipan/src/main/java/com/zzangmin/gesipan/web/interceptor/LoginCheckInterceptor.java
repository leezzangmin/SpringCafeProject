package com.zzangmin.gesipan.web.interceptor;

import com.zzangmin.gesipan.web.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isRequestNeedsAuthorization(request)) {
            validateAuth(request);
        }
        return true;
    }

    private boolean isRequestNeedsAuthorization(HttpServletRequest request) {
        String currentURI = request.getRequestURI();
        String currentMethod = request.getMethod();
        if ((Pattern.matches("^/post/\\d+$", currentURI) && currentMethod.equals("GET")) ||
        (Pattern.matches("/posts", currentURI)) && currentMethod.equals("GET")) {
            return false;
        }
        return true;
    }

    private void validateAuth(HttpServletRequest request) {
        String jwt = jwtProvider.resolveToken(request)
                .orElseThrow(() -> new IllegalArgumentException("인증 수단이 존재하지 않습니다."));
        if (!jwtProvider.isValidToken(jwt)) {
            throw new IllegalArgumentException("인증이 유효하지 않습니다.");
        }
    }
    
}
