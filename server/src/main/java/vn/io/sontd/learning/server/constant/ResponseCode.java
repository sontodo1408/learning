package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Business-level status codes carried in {@link vn.io.sontd.learning.server.response.ResponseRoot#getCode()},
 * independent of the HTTP status code (which is typically always 200 for this API).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseCode {
    /** Request completed successfully. */
    public static final Integer SUCCESS = 0;
    /** Unexpected server-side error. */
    public static final Integer SERVER_ERROR = 500;
    /** Expected/handled business rule violation (e.g. validation failure). */
    public static final Integer BUSINESS_ERROR = 400;
    /** Authentication or authorization failure. */
    public static final Integer ACCESS_DENIED = 401;
}
