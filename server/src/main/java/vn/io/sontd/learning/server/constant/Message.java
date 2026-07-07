package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {
    public static final String AUTH_ERROR = "";
    public static final String ACCESS_DENIED = "";
    public static final String SYS_ERROR = "Lỗi hệ thống. Vui lòng thử lại sau!";
    public static final String LOGIN_FAIL = "Login thất bại. Hãy kiểm tra lại username và password!";
}
