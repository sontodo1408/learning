package vn.io.sontd.learning.server.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Miscellaneous application-wide constants: security-related values and
 * HTTP header names shared across controllers/filters.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    // SECURITY
    /** URL patterns that bypass JWT authentication entirely (see JwtAuthenticationFilter). */
    public static final String[] INTERNAL_PERMIT_ALL = {"/api/v1/auth/login", "/api/v1/auth/google-login", "/api/v1/study-sets/**", "/api/v1/home/recent-study-sets", "/api/v1/home/newest-study-sets", "/api/v1/imgs/**", "/test/**"};
    /** JWT claim name used to carry the user's DB-encoded password. */
    public static final String PASSWORD_CLAIM = "password_claim";

    // HEADER
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    public static final String X_SCHOOL_ID = "X-School-Id";
    public static final String X_SCHOOL_YEAR = "X-School-Year";
    public static final String X_SEMESTER = "X-Semester";

    // IMAGE STORAGE
    /**
     * Public URL path prefix under which uploaded images are served. {@code study_cards.img_url}
     * only ever persists the bare, generated filename (e.g. {@code ab12cd34.png}); this prefix is
     * prepended/stripped by {@code ImageStorageServiceImpl} when converting to/from the public URL
     * a client actually calls (e.g. {@code /api/v1/imgs/ab12cd34.png}). Kept in sync between the
     * resource handler (WebConfig) and the permit-all list above ({@code /api/v1/imgs/**}).
     */
    public static final String IMAGE_URL_PREFIX = "/api/v1/imgs";

    // STUDY SET
    /**
     * Title prefix identifying an auto-generated "Daily Vocabulary" study set; the full title
     * is this prefix immediately followed by the study set's id (see {@code StudySetServiceImpl.saveStudySet}).
     * Also used to search for these study sets by title (see {@code StudySetServiceImpl.findRecentlyCreated}
     * and {@code AdminVideoVocabController.getDailyVocabularySets}).
     */
    public static final String DAILY_VOCAB_TITLE_PREFIX = "Daily English Vocab #";
    /**
     * Description prefix for an auto-generated "Daily Vocabulary" study set; the full description
     * is this prefix immediately followed by the study set's id (see {@code StudySetServiceImpl.saveStudySet}).
     */
    public static final String DAILY_VOCAB_DESCRIPTION_PREFIX = "Daily English with Mr.Son - Vocabulary #";
}
