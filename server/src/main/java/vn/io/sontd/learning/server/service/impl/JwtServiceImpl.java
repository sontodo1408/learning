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

/**
 * {@link JwtService} implementation using the JJWT library with HMAC signing.
 * Signing key and token lifetime come from {@code thesis.app.jwt-secret} and
 * {@code thesis.app.jwt-expiration-ms} in {@code application.properties}.
 */
@Component
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${thesis.app.jwt-secret}")
    private String jwtSecret;

    @Value("${thesis.app.jwt-expiration-ms}")
    private int jwtExpirationMs;

    /**
     * {@inheritDoc}
     * The subject is the username; the password claim carries the DB-encoded
     * password so {@code JwtAuthenticationFilter} can detect password changes.
     */
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

    /**
     * {@inheritDoc}
     * Any parsing failure (invalid signature, malformed token, etc.) is
     * logged and treated as "no username" rather than propagated.
     */
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

    /**
     * {@inheritDoc}
     * Any parsing failure is logged and treated as "no password" rather than propagated.
     */
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

    /**
     * {@inheritDoc}
     * Expects the header value to be {@code "Bearer <token>"}.
     */
    @Override
    public String getAuthToken(HttpServletRequest httpReq) {
        // Read the raw Authorization header from the request
        final String authHeader = httpReq.getHeader(Constant.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(Constant.BEARER)
                && authHeader.length() > 7) {
            return authHeader.substring(7);
        }
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     * Logs the specific validation failure reason (malformed, expired, unsupported, or empty claims).
     */
    @Override
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser() // JJWT 0.13 API: parser() replaces the old parserBuilder()
                    .verifyWith(key()) // replaces the old setSigningKey()
                    .build().parse(authToken); // replaces the old parseClaimsJws(token)
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

    /**
     * Builds the HMAC signing key from the configured secret.
     */
    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
