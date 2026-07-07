package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableField {
    // table name
    public static final String TBL_USERS = "users";

    // query
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";

    // base entity
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String IS_DELETE = "is_delete";

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FULL_NAME = "full_name";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String BIRTHDAY = "birthday";
    public static final String GENDER = "gender";
    public static final String ROLE = "role";
    public static final String STATUS = "status";

}
