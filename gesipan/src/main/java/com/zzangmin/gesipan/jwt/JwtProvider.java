package com.zzangmin.gesipan.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Optional;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${JWT_SECRET}")
    private String secretKey;
    private final String claimSubject = "cafe_payload_subject";

    // 토큰 유효시간 30분
    private final long tokenValidTime = 30 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Optional<Long> getUserId (HttpServletRequest request) {
        if (isLoginStatus(request)) {
            return Optional.of(getUserIdFromToken(resolveToken(request).get()));
        }
        return Optional.empty();
    }

    public String createToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(claimSubject);
        claims.put("userId", userId);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public long getUserIdFromToken(String jwt) {
        return Long.parseLong(Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody()
                .get("userId")
                .toString());
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(i -> i.getName().equals("X-AUTH-TOKEN"))
                .findFirst()
                .map(i -> i.getValue());
    }

    public boolean isValidToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
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
