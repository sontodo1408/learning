package vn.io.sontd.learning.server.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.io.sontd.learning.server.constant.Constant;
import vn.io.sontd.learning.server.dto.auth.TokenInfoDTO;
import vn.io.sontd.learning.server.service.JwtService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${thesis.app.jwt-secret}")
    private String jwtSecret;

    @Value("${thesis.app.jwt-expiration-ms}")
    private int jwtExpirationMs;

    @Override
    public String generateJwtToken(TokenInfoDTO tokenInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.PASSWORD_CLAIM, tokenInfo.getPassword());

        return Jwts.builder() //
                .subject((tokenInfo.getUsername())) //
                .issuedAt(new Date()) //
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(key())
                .claims(claims) //
                .compact();
    }

    @Override
    public String getUsername(String token) {
        try {
            return Jwts.parser() //
                    .verifyWith(key()) //
                    .build() //
                    .parseSignedClaims(token)   //
                    .getPayload() //
                    .getSubject();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String getPassword(String token) {
        try {
            return Jwts.parser() //
                    .verifyWith(key()) //
                    .build() //
                    .parseSignedClaims(token)   //
                    .getPayload() //
                    .get(Constant.PASSWORD_CLAIM, String.class);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String getAuthToken(HttpServletRequest httpReq) {
        // HEADERからトークンを取得します。
        final String authHeader = httpReq.getHeader(Constant.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(Constant.BEARER)
                && authHeader.length() > 7) {
            return authHeader.substring(7);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser() // thay parserBuilder()
                    .verifyWith(key()) // thay setSigningKey()
                    .build().parse(authToken); // thay parseClaimsJws(token)
            return true;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
