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
    /** Message returned when saving a study set with an id that doesn't exist. */
    public static final String STUDY_SET_NOT_FOUND = "Không tìm thấy study set với id đã cho!";
    /** Message returned when a JSON string can't be parsed into the target type (see {@code CommonUtils.parseJson}). */
    public static final String INVALID_JSON = "Dữ liệu JSON không hợp lệ!";
    /** Message returned when a card references an uploaded file index that doesn't exist in the request. */
    public static final String IMAGE_FILE_INDEX_INVALID = "Chỉ số file ảnh không hợp lệ!";
    /** Message returned when an uploaded image can't be stored (empty file or I/O error). */
    public static final String IMAGE_UPLOAD_FAIL = "Lưu ảnh thất bại. Vui lòng thử lại!";
}
