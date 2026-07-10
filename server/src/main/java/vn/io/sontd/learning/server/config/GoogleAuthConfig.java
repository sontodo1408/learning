package vn.io.sontd.learning.server.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Exposes a {@link GoogleIdTokenVerifier} used to verify ID tokens issued by
 * Google Sign-In (signature, issuer, expiry, and audience), for the
 * "Login with Google" flow.
 */
@Configuration
public class GoogleAuthConfig {

    /**
     * Builds the verifier, restricting accepted tokens to the ones issued for
     * this application's OAuth client (the {@code aud} claim).
     *
     * @param googleClientId this application's Google OAuth client ID (see {@code thesis.app.google-client-id})
     * @return the configured verifier
     */
    @Bean
    GoogleIdTokenVerifier googleIdTokenVerifier(@Value("${thesis.app.google-client-id}") String googleClientId) {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }
}
