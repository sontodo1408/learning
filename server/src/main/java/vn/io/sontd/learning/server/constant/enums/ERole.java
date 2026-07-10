package vn.io.sontd.learning.server.constant.enums;

import lombok.Getter;

/**
 * Application roles. {@link #value} is the Spring Security authority string
 * (must keep the {@code ROLE_} prefix expected by role-based access checks).
 */
@Getter
public enum ERole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;

    ERole(String value) {
        this.value = value;
    }
}
