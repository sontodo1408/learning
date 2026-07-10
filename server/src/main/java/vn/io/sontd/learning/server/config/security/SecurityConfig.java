package vn.io.sontd.learning.server.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.io.sontd.learning.server.constant.Constant;
import vn.io.sontd.learning.server.service.JwtService;

import java.util.Arrays;
import java.util.List;

/**
 * Central Spring Security configuration: stateless JWT-based authentication,
 * CORS, password encoding, and the JSON error handlers used instead of the
 * framework's default HTML error pages.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * Builds the custom JWT authentication filter used before
     * {@link UsernamePasswordAuthenticationFilter} in the chain.
     */
    @Bean
    JwtAuthenticationFilter jwtAuthFilter() {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    /**
     * Configures the main HTTP security filter chain: CORS, disabled CSRF
     * (not needed for a stateless token-based API), public/authenticated URL
     * rules, stateless sessions, and the custom JWT filter/error handlers.
     *
     * @param http the security configuration builder
     * @return the built filter chain
     */
    @Bean
    SecurityFilterChain internalSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) //
                .csrf(AbstractHttpConfigurer::disable) //
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.requestMatchers(Constant.INTERNAL_PERMIT_ALL).permitAll().anyRequest().authenticated()) //
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(authenticationProvider());

        // Custom JSON error handling for authentication/authorization failures
        http.exceptionHandling(configure -> {
            configure.authenticationEntryPoint(authenticationEntryPoint());
            configure.accessDeniedHandler(accessDeniedHandler());
        });

        http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Handles requests that fail authentication (missing/invalid token).
     */
    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new FailAuthenticationEntryPoint();
    }

    /**
     * Handles requests that are authenticated but not authorized.
     */
    @Bean
    AccessDeniedHandler accessDeniedHandler() {
        return new FailAccessDeniedHandler();
    }

    /**
     * Looks up users and verifies credentials via {@link #userDetailsService}
     * and {@link #passwordEncoder()}.
     * <p>
     * The encoder must be set explicitly — otherwise {@link DaoAuthenticationProvider}
     * falls back to Spring Security's default {@code DelegatingPasswordEncoder}, which
     * requires an {@code {id}} prefix on stored passwords and rejects raw BCrypt hashes.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Exposes Spring Security's {@link AuthenticationManager} as a bean so it
     * can be injected into services (e.g. for login).
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Password hashing/verification strategy used for stored user passwords.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS policy for the API: only the known frontend origins are allowed,
     * credentials (cookies/auth headers) are permitted, and standard REST
     * verbs are enabled.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:9000", "https://sontd.io.vn", "http://192.168.19.57:9000")); // frontend URL
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
