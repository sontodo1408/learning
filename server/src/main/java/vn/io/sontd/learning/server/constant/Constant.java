package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Miscellaneous application-wide constants: security-related values and
 * HTTP header names shared across controllers/filters.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    // SECURITY
    /** URL patterns that bypass JWT authentication entirely (see JwtAuthenticationFilter). */
    public static final String[] INTERNAL_PERMIT_ALL = {"/api/auth/login", "/test/**"};
    /** JWT claim name used to carry the user's DB-encoded password. */
    public static final String PASSWORD_CLAIM = "password_claim";

    // HEADER
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String X_SCHOOL_ID = "X-School-Id";
    public static final String X_SCHOOL_YEAR = "X-School-Year";
    public static final String X_SEMESTER = "X-Semester";
}
