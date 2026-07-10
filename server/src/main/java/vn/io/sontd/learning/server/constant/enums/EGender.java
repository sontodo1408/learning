package vn.io.sontd.learning.server.constant.enums;

import lombok.Getter;

/**
 * Gender option for a {@link vn.io.sontd.learning.server.entity.UserEntity},
 * persisted as its numeric {@link #value} (see {@code users.gender} column).
 */
@Getter
public enum EGender {
    MAN(1),
    WOMAN(2),
    OTHER(3);

    private final Integer value;

    EGender(Integer value) {
        this.value = value;
    }
}
