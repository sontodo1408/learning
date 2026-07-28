package vn.io.sontd.learning.server.service;

import org.springframework.web.multipart.MultipartFile;
import vn.io.sontd.learning.server.dto.studyset.StudySetDTO;
import vn.io.sontd.learning.server.request.studyset.MyStudySetUpsertRequest;
import vn.io.sontd.learning.server.request.studyset.StudySetUpsertRequest;

import java.util.List;

/**
 * Study set lookups and mutations shared across controllers.
 */
public interface StudySetService {

    /**
     * Finds every study set whose title contains the given substring, each
     * with its study cards attached.
     *
     * @param title the substring to search for within {@code study_sets.title}
     * @return matching study sets, as frontend-facing DTOs
     */
    List<StudySetDTO> findByTitleContaining(String title);

    /**
     * Searches public study sets whose title/description contains the given keyword, or that
     * own at least one study card whose term/definition contains it (all case-insensitive),
     * each with its study cards attached. Study sets with {@code isPublic = false} are excluded.
     *
     * @param keyword the substring to search for
     * @return matching public study sets, as frontend-facing DTOs
     */
    List<StudySetDTO> search(String keyword);

    /**
     * Finds a study set by id, together with its study cards.
     *
     * @param studySetId the study set's id
     * @return the matching study set, as a frontend-facing DTO
     * @throws vn.io.sontd.learning.server.exception.BusinessException if no study set has this id
     */
    StudySetDTO findById(Long studySetId);

    /**
     * Finds the study sets with the given ids, each with its study cards attached.
     * The result preserves the order of {@code ids} (so callers can pass an
     * already-sorted id list, e.g. "most recently viewed first"), and silently
     * drops any id that no longer resolves to a study set.
     *
     * @param ids the study set ids to fetch, in the desired result order
     * @return the matching study sets as frontend-facing DTOs, in the same order as {@code ids}
     */
    List<StudySetDTO> findByIds(List<Long> ids);

    /**
     * Finds the most recently created "Daily Vocabulary" study sets (owned by
     * {@link vn.io.sontd.learning.server.constant.Constant#VIDEO_VOCAB_USER_ID}),
     * each with its study cards attached.
     *
     * @param limit the maximum number of study sets to return; non-positive values yield an empty list
     * @return up to {@code limit} study sets, ordered by creation time descending
     */
    List<StudySetDTO> findRecentlyCreated(int limit);

    /**
     * Finds every study set owned by the given user id, each with its study cards attached.
     *
     * @param userId the owning user's id (see {@code study_sets.user_id})
     * @return matching study sets, ordered by creation time descending
     */
    List<StudySetDTO> findByUserId(Long userId);

    /**
     * Creates or updates a study set together with its cards.
     * If {@code request.getId()} is present, the matching study set is
     * updated and all of its existing cards are deleted and replaced by
     * {@code request.getStudyCards()}. If it's {@code null}, a new study set
     * (and all of its cards) is created.
     * <p>
     * Each card may reference a newly uploaded image via
     * {@code StudyCardUpsertRequest.imageFileIndex} (a 0-based index into
     * {@code files}); referenced files are stored and the card's
     * {@code imgUrl} is set to the stored image's public URL.
     *
     * @param request the study set (and cards) to save
     * @param files   the uploaded image files referenced by the cards, or {@code null}/empty if none
     * @return the saved study set, as a frontend-facing DTO
     */
    StudySetDTO saveStudySet(StudySetUpsertRequest request, List<MultipartFile> files);

    /**
     * Creates or updates a study set (together with its cards) owned by the current
     * authenticated user, unlike {@link #saveStudySet}, which always saves under the
     * "video vocab" system account with an auto-generated title/description.
     * {@code title}, {@code description} and {@code isPublic} are taken as-is from
     * {@code request}; {@code userId} is always the current user's id.
     * <p>
     * If {@code request.getId()} is present, the matching study set is updated (its existing
     * cards deleted and replaced by {@code request.getStudyCards()}) — but only if it's
     * currently owned by the current user. If {@code request.getId()} is {@code null}, a new
     * study set owned by the current user is created.
     * <p>
     * Cards may reference newly uploaded images the same way as {@link #saveStudySet} does.
     *
     * @param request the study set (and cards) to save
     * @param files   the uploaded image files referenced by the cards, or {@code null}/empty if none
     * @return the saved study set, as a frontend-facing DTO
     * @throws vn.io.sontd.learning.server.exception.BusinessException if the caller isn't logged in,
     *                                                                 {@code request.getId()} doesn't resolve to a study set, or it's not owned by the current user
     */
    StudySetDTO saveMyStudySet(MyStudySetUpsertRequest request, List<MultipartFile> files);
}
