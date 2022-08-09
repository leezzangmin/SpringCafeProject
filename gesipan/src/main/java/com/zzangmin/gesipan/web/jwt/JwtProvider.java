package com.zzangmin.gesipan.web.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

// https://velog.io/@kyeongsoo5196/JWT%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%94%8C%EB%A1%9C%EC%9A%B0-%EC%97%B0%EA%B5%AC
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private String secretKey = System.getenv("JWT_SECRET");
    private final String claimSubject = "cafe_payload_subject";

    // 토큰 유효시간 30분
    private final long tokenValidTime = 30 * 60 * 1000L;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userEmail) {
        Claims claims = Jwts.claims().setSubject(claimSubject); // JWT payload 에 저장되는 정보단위
        claims.put("jwtEMAIL", userEmail); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                .compact();
    }

//    // JWT 토큰에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        //UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//        return null;//new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    public String getUserInfo(String jwt) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody()
                .get("jwtEMAIL")
                .toString();
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(i -> i.getName().equals("X-AUTH-TOKEN"))
                .findFirst()
                .map(i -> i.getValue());
    }

    public boolean isValidToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
