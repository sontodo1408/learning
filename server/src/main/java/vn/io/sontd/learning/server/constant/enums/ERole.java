package vn.io.sontd.learning.server.constant.enums;

public enum ERole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
