package vn.io.sontd.learning.server.constant.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum EUserStatus {
    INACTIVE, //
    ACTIVE
}
