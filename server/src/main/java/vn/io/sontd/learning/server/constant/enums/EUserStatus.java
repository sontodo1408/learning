package vn.io.sontd.learning.server.constant.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Account status for a {@link vn.io.sontd.learning.server.entity.UserEntity}.
 * Serialized as its ordinal integer (see {@code users.status} column).
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum EUserStatus {
    INACTIVE, //
    ACTIVE
}
