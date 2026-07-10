package vn.io.sontd.learning.server.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.io.sontd.learning.server.repository.UserRepository;

/**
 * Loads a user's Spring Security {@link UserDetails} by username, backed by
 * {@link UserRepository}. Used by both the authentication provider (login)
 * and {@link JwtAuthenticationFilter} (per-request token validation).
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Looks up a user by username and wraps it as a {@link UserDetailsImpl}.
     *
     * @param username the username to look up
     * @return the matching user's details
     * @throws UsernameNotFoundException if no user exists with that username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(UserDetailsImpl::build)
                .orElseThrow();
    }
}
