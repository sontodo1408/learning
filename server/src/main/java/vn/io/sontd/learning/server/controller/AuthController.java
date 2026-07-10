package vn.io.sontd.learning.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.io.sontd.learning.server.request.auth.GoogleLoginRequest;
import vn.io.sontd.learning.server.request.auth.LoginRequest;
import vn.io.sontd.learning.server.response.ResponseRoot;
import vn.io.sontd.learning.server.service.AuthService;

/**
 * Authentication endpoints: login and current-session verification.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthService authService;

    /**
     * Authenticates a user by username/password and issues a JWT.
     *
     * @param request the login credentials
     * @return the user's profile (minus password) plus the JWT token
     */
    @PostMapping("/login")
    public ResponseRoot login(@RequestBody LoginRequest request) {
        return success(authService.login(request));
    }

    /**
     * Authenticates a user via a Google ID token and issues a JWT, registering
     * a new account on the fly if this is the user's first Google sign-in.
     *
     * @param request carries the Google ID token obtained by the client after Google Sign-In
     * @return the user's profile (minus password) plus the JWT token
     */
    @PostMapping("/google-login")
    public ResponseRoot googleLogin(@RequestBody GoogleLoginRequest request) {
        return success(authService.googleLogin(request));
    }

    /**
     * Returns the profile of the currently authenticated user.
     * Reaching this endpoint at all already proves the bearer token is valid,
     * since {@link vn.io.sontd.learning.server.config.security.JwtAuthenticationFilter}
     * runs before it.
     *
     * @return the current user's profile (minus password)
     */
    @GetMapping("/check-login")
    public ResponseRoot checkLogin() {
        return success(authService.checkLogin());
    }
}
