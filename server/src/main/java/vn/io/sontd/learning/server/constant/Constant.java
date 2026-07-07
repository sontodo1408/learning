package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    // SECURITY
    public static final String[] INTERNAL_PERMIT_ALL = {"/api/v1/auth/login",
            "/api/v1/auth/init", "/test/**"};
    public static final String PASSWORD_CLAIM = "password_claim";

    // HEADER
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String X_SCHOOL_ID = "X-School-Id";
    public static final String X_SCHOOL_YEAR = "X-School-Year";
    public static final String X_SEMESTER = "X-Semester";
}
