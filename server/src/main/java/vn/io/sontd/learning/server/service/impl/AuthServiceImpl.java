package vn.io.sontd.learning.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.io.sontd.learning.server.config.security.UserDetailsImpl;
import vn.io.sontd.learning.server.constant.Message;
import vn.io.sontd.learning.server.dto.auth.TokenInfoDTO;
import vn.io.sontd.learning.server.entity.UserEntity;
import vn.io.sontd.learning.server.repository.UserRepository;
import vn.io.sontd.learning.server.request.auth.LoginRequest;
import vn.io.sontd.learning.server.response.auth.LoginResponse;
import vn.io.sontd.learning.server.service.AuthService;
import vn.io.sontd.learning.server.service.JwtService;

/**
 * Default {@link AuthService} implementation, delegating credential checks to
 * Spring Security's {@link AuthenticationManager} and token issuance to {@link JwtService}.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

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
