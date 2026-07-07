package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseCode {
    public static final Integer SUCCESS = 0;
    public static final Integer SERVER_ERROR = 500;
    public static final Integer BUSINESS_ERROR = 400;
    public static final Integer ACCESS_DENIED = 401;
}
