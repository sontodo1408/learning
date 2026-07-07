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
import org.springframework.web.filter.OncePerRequestFilter;
import vn.io.sontd.learning.server.constant.Constant;
import vn.io.sontd.learning.server.service.JwtService;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authToken = jwtService.getAuthToken(request);
        String urlPath = request.getRequestURI();
        boolean urlCheck = Arrays.stream(Constant.INTERNAL_PERMIT_ALL).parallel() //
                .anyMatch(urlPath::equals);

        // APIKEYの存在確認。
        if (StringUtils.isBlank(authToken) || urlCheck) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtService.validateJwtToken(authToken)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        // トークン情報を抽出する
        String username = jwtService.getUsername(authToken);
        String password = jwtService.getPassword(authToken);

        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
                    username);

            // パスワードの確認
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
