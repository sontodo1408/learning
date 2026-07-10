package vn.io.sontd.learning.server.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for {@code POST /api/v1/auth/google-login}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginRequest {
    /** The Google ID token obtained on the client after a successful Google Sign-In. */
    private String idToken;
}
