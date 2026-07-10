package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Table and column name constants mirroring {@code db/create.sql}.
 * Every JPA {@code @Column(name = ...)} must reference one of these instead
 * of a string literal, so schema/entity drift is easy to spot and fix.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableField {
    // table name
    public static final String TBL_USERS = "users";
    public static final String TBL_STUDY_SETS = "study_sets";
    public static final String TBL_STUDY_CARDS = "study_cards";

    // pagination query params
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";

    // columns shared by every entity via BaseEntity
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String IS_DELETE = "is_delete";

    // users table columns
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FULL_NAME = "full_name";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String BIRTHDAY = "birthday";
    public static final String GENDER = "gender";
    public static final String ROLE = "role";
    public static final String STATUS = "status";

    // study_sets table columns
    public static final String USER_ID = "user_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String IS_PUBLIC = "is_public";

    // study_cards table columns
    public static final String STUDY_SET_ID = "study_set_id";
    public static final String TERM = "term";
    public static final String DEFINITION = "definition";
    public static final String PRONOUNCE = "pronounce";
    public static final String IMG_URL = "img_url";
    public static final String DISPLAY_ORDER = "display_order";

}
