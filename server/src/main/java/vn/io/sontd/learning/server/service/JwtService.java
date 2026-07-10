package vn.io.sontd.learning.server.service;

import jakarta.servlet.http.HttpServletRequest;
import vn.io.sontd.learning.server.dto.auth.TokenInfoDTO;

/**
 * JWT issuance/parsing/validation for the stateless authentication scheme.
 */
public interface JwtService {

    /**
     * Signs a new JWT for the given username/password pair.
     *
     * @param tokenInfo carries the subject (username) and password claim
     * @return the compact, signed JWT string
     */
    String generateJwtToken(TokenInfoDTO tokenInfo);

    /**
     * Extracts the username (subject) from a JWT.
     *
     * @param token the JWT to parse
     * @return the username, or empty string if the token is invalid/unparsable
     */
    String getUsername(String token);

    /**
     * Extracts the password claim from a JWT.
     *
     * @param token the JWT to parse
     * @return the encoded password, or empty string if the token is invalid/unparsable
     */
    String getPassword(String token);

    /**
     * Extracts the bearer token from the {@code Authorization} header of a request.
     *
     * @param httpReq the incoming HTTP request
     * @return the raw token string, or empty string if no bearer token is present
     */
    String getAuthToken(HttpServletRequest httpReq);

    /**
     * Verifies a JWT's signature and expiration.
     *
     * @param authToken the JWT to validate
     * @return true if the token is well-formed, signed correctly, and not expired
     */
    boolean validateJwtToken(String authToken);
}
