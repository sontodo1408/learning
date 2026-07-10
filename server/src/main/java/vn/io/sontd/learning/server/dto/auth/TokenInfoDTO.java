package vn.io.sontd.learning.server.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Internal data carrier used only to generate a JWT (see {@code JwtService}).
 * Not exposed via any controller as a request/response body.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfoDTO {
    /** Becomes the token's subject claim. */
    private String username;
    /** Becomes the token's password claim; must be the DB-encoded password. */
    private String password;
}
