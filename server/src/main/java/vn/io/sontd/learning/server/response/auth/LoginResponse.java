package vn.io.sontd.learning.server.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.io.sontd.learning.server.constant.enums.EGender;
import vn.io.sontd.learning.server.constant.enums.EUserStatus;
import vn.io.sontd.learning.server.response.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response payload for {@code POST /api/v1/auth/login} and {@code GET /api/v1/auth/check-login}.
 * Mirrors every {@code UserEntity} field except the password.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse extends ResponseBody {
    /** JWT issued on login; {@code null} for the check-login endpoint (no new token issued). */
    private String token;
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate birthday;
    private EGender gender;
    private String role;
    private EUserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
