package vn.io.sontd.learning.server.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.io.sontd.learning.server.constant.Constant;
import vn.io.sontd.learning.server.service.JwtService;

import java.io.IOException;
import java.util.Arrays;

/**
 * Runs once per request, before {@link org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter},
 * to authenticate the caller from a JWT bearer token.
 * <p>
 * The token's password claim (set at login time to the user's DB-encoded password) is
 * compared against {@link UserDetailsImpl#getPassword()} so that changing/resetting a
 * user's password immediately invalidates any previously issued tokens.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /** Matches {@link Constant#INTERNAL_PERMIT_ALL} entries the same way {@code requestMatchers} does, incl. {@code /**} wildcards. */
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Validates the bearer token (if any) and, when valid, populates the
     * {@link SecurityContextHolder} with an authenticated principal before
     * continuing the filter chain.
     *
     * @param request     the incoming HTTP request
     * @param response    the outgoing HTTP response
     * @param filterChain the remaining filter chain to invoke
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authToken = jwtService.getAuthToken(request);

        // No token at all → proceed anonymously; permit-all URLs allow this, and
        // authenticated ones will be rejected downstream by the authorization rules.
        if (StringUtils.isBlank(authToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtService.validateJwtToken(authToken)) {
            String urlPath = request.getRequestURI();
            boolean urlIsPublic = Arrays.stream(Constant.INTERNAL_PERMIT_ALL) //
                    .anyMatch(pattern -> PATH_MATCHER.match(pattern, urlPath));

            // On a permit-all URL, an invalid/expired token doesn't fail the request —
            // it's simply treated the same as no token (anonymous) instead of rejected.
            if (urlIsPublic) {
                filterChain.doFilter(request, response);
                return;
            }
            throw new BadCredentialsException("Invalid API Key");
        }

        // Extract identity information carried by the token
        String username = jwtService.getUsername(authToken);
        String password = jwtService.getPassword(authToken);

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
                    username);

            // Reject the token if its embedded password no longer matches the current DB password
            if (userDetails.getPassword().equals(password)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
