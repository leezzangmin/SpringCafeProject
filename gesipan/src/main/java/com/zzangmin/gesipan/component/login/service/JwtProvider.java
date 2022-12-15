package com.zzangmin.gesipan.component.login.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.servlet.http.Cookie;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class JwtProvider {

    @Value("${JWT_SECRET}")
    private String JWT_SECRET_KEY;
    private final String claimSubject = "cafe_payload_subject";
    private final String PRIVATE_PAYLOAD_CLAIM_NAME = "userId";
    private final String CAFE_TOKEN_NAME = "X-AUTH-TOKEN";

    // 토큰 유효시간 30분
    private final long tokenValidTime = 30 * 60 * 1000L;

    public JwtProvider(String JWT_SECRET_KEY) {
        this.JWT_SECRET_KEY = JWT_SECRET_KEY;
    }

    @PostConstruct
    protected void init() {
        JWT_SECRET_KEY = Base64.getEncoder().encodeToString(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public Optional<Long> getUserId (HttpServletRequest request) {
        if (isLoginStatus(request)) {
            return Optional.of(getUserIdFromToken(resolveToken(request).get()));
        }
        return Optional.empty();
    }

    public String createToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(claimSubject);
        claims.put(PRIVATE_PAYLOAD_CLAIM_NAME, userId);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public long getUserIdFromToken(String jwt) {
        return Long.parseLong(Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody()
                .get(PRIVATE_PAYLOAD_CLAIM_NAME)
                .toString());
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(i -> i.getName().equals(CAFE_TOKEN_NAME))
                .findFirst()
                .map(i -> i.getValue());
    }

    public boolean isValidToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(jwtToken);

            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isLoginStatus(HttpServletRequest request) {
        Optional<String> jwt = resolveToken(request);
        if (jwt.isEmpty()) {
            return false;
        }
        if (!isValidToken(jwt.get())) {
            return false;
        }
        return true;
    }
}
