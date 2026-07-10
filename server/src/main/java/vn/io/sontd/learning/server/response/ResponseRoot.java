package vn.io.sontd.learning.server.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard JSON envelope returned by every API endpoint.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseRoot {
    /** Business status code, see {@link vn.io.sontd.learning.server.constant.ResponseCode}. */
    private Integer code;
    /** Optional data payload for successful/erroring responses. */
    private ResponseBody payload;
    /** Human-readable message, see {@link vn.io.sontd.learning.server.constant.Message}. */
    private String msg;
}
