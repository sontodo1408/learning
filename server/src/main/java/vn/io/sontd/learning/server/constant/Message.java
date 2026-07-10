package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * User-facing message strings returned to the client inside a
 * {@link vn.io.sontd.learning.server.response.ResponseRoot}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {
    /** Generic authentication failure message (e.g. invalid/expired token). */
    public static final String AUTH_ERROR = "";
    /** Message returned when an authenticated user lacks permission for an action. */
    public static final String ACCESS_DENIED = "";
    /** Fallback message for unexpected server-side errors. */
    public static final String SYS_ERROR = "Lỗi hệ thống. Vui lòng thử lại sau!";
    /** Message returned when login credentials are invalid. */
    public static final String LOGIN_FAIL = "Login thất bại. Hãy kiểm tra lại username và password!";
    /** Message returned when a Google ID token fails verification (invalid, expired, or wrong audience). */
    public static final String GOOGLE_LOGIN_FAIL = "Đăng nhập Google thất bại. Token không hợp lệ hoặc đã hết hạn!";
}
