package vn.io.sontd.learning.server.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.io.sontd.learning.server.config.security.UserDetailsImpl;
import vn.io.sontd.learning.server.constant.Message;
import vn.io.sontd.learning.server.constant.enums.ERole;
import vn.io.sontd.learning.server.constant.enums.EUserStatus;
import vn.io.sontd.learning.server.dto.auth.TokenInfoDTO;
import vn.io.sontd.learning.server.entity.UserEntity;
import vn.io.sontd.learning.server.repository.UserRepository;
import vn.io.sontd.learning.server.request.auth.GoogleLoginRequest;
import vn.io.sontd.learning.server.request.auth.LoginRequest;
import vn.io.sontd.learning.server.response.auth.LoginResponse;
import vn.io.sontd.learning.server.service.AuthService;
import vn.io.sontd.learning.server.service.JwtService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

/**
 * Default {@link AuthService} implementation, delegating credential checks to
 * Spring Security's {@link AuthenticationManager} and token issuance to {@link JwtService}.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    /** Column width limits from {@code db/create.sql}, enforced here to avoid a data-truncation error on save. */
    private static final int USERNAME_MAX_LENGTH = 50;
    private static final int FULL_NAME_MAX_LENGTH = 25;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     * On invalid credentials, translates any {@link AuthenticationException}
     * into a {@link BadCredentialsException} carrying {@link Message#LOGIN_FAIL}.
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        UserDetailsImpl userDetails;
        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword());
            userDetails = (UserDetailsImpl) authenticationManager.authenticate(authRequest).getPrincipal();
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException(Message.LOGIN_FAIL, ex);
        }

        // Token password claim must carry the encoded (DB) password, since
        // JwtAuthenticationFilter re-checks it against UserDetails.getPassword().
        String token = jwtService.generateJwtToken(new TokenInfoDTO(userDetails.getUsername(), userDetails.getPassword()));

        return toResponse(token, findUser(userDetails.getUsername()));
    }

    /**
     * {@inheritDoc}
     * Verifies the ID token's signature, issuer, expiry, and audience via
     * {@link GoogleIdTokenVerifier}, then matches it to an existing user by
     * email or transparently registers a new account.
     */
    @Override
    public LoginResponse googleLogin(GoogleLoginRequest request) {
        GoogleIdToken.Payload payload = verifyGoogleIdToken(request.getIdToken());

        UserEntity user = userRepository.findByEmail(payload.getEmail())
                .orElseGet(() -> registerGoogleUser(payload));

        String token = jwtService.generateJwtToken(new TokenInfoDTO(user.getUsername(), user.getPassword()));

        return toResponse(token, user);
    }

    /**
     * Verifies a Google ID token and returns its payload.
     * Translates any failure (invalid signature/issuer/audience/expiry, or an
     * unverified email) into a {@link BadCredentialsException}.
     */
    private GoogleIdToken.Payload verifyGoogleIdToken(String idTokenString) {
        GoogleIdToken idToken;
        try {
            idToken = googleIdTokenVerifier.verify(idTokenString);
        } catch (GeneralSecurityException | IOException ex) {
            throw new BadCredentialsException(Message.GOOGLE_LOGIN_FAIL, ex);
        }

        if (idToken == null || !Boolean.TRUE.equals(idToken.getPayload().getEmailVerified())) {
            throw new BadCredentialsException(Message.GOOGLE_LOGIN_FAIL);
        }

        return idToken.getPayload();
    }

    /**
     * Creates a new account for a first-time Google sign-in.
     * The password is a random value the user never sees; it only exists to
     * satisfy the {@code users.password} NOT NULL constraint, since this
     * account is meant to be accessed via Google Sign-In going forward.
     */
    private UserEntity registerGoogleUser(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        UserEntity user = new UserEntity();
        user.setUsername(StringUtils.left(email, USERNAME_MAX_LENGTH));
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setFullName(StringUtils.left(StringUtils.defaultIfBlank(name, email), FULL_NAME_MAX_LENGTH));
        user.setEmail(email);
        user.setRole(ERole.USER.getValue());
        user.setStatus(EUserStatus.ACTIVE);
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     * No new token is issued; the caller already proved possession of a
     * valid token by reaching this authenticated endpoint.
     */
    @Override
    public LoginResponse checkLogin() {
        String username = ((UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        return toResponse(null, findUser(username));
    }

    /**
     * Fetches the full user entity by username (the {@link UserDetailsImpl}
     * used for authentication doesn't carry every profile field, e.g. gender/timestamps).
     */
    private UserEntity findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    /**
     * Maps a {@link UserEntity} to a {@link LoginResponse}, deliberately
     * omitting the password field.
     */
    private LoginResponse toResponse(String token, UserEntity user) {
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getFullName(),
                user.getEmail(), user.getPhoneNumber(), user.getBirthday(), user.getGender(),
                user.getRole(), user.getStatus(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
