package vn.io.sontd.learning.server.service;

import vn.io.sontd.learning.server.request.auth.GoogleLoginRequest;
import vn.io.sontd.learning.server.request.auth.LoginRequest;
import vn.io.sontd.learning.server.response.auth.LoginResponse;

/**
 * Authentication use cases: login and current-session verification.
 */
public interface AuthService {

    /**
     * Authenticates a user by username/password and issues a JWT.
     *
     * @param request the login credentials
     * @return the user's profile (minus password) plus the generated JWT
     */
    LoginResponse login(LoginRequest request);

    /**
     * Authenticates a user via a Google ID token and issues a JWT. If no user
     * exists yet for the token's email, a new account is created on the fly.
     *
     * @param request carries the Google ID token obtained by the client after Google Sign-In
     * @return the user's profile (minus password) plus the generated JWT
     */
    LoginResponse googleLogin(GoogleLoginRequest request);

    /**
     * Returns the profile of the currently authenticated user (from the
     * security context populated by the JWT filter).
     *
     * @return the current user's profile (minus password), with no token
     */
    LoginResponse checkLogin();
}
