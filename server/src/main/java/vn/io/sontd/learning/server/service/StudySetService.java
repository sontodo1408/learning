package vn.io.sontd.learning.server.service;

import org.springframework.web.multipart.MultipartFile;
import vn.io.sontd.learning.server.dto.studyset.StudySetDTO;
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
     * Finds the most recently created "Daily Vocabulary" study sets (title containing
     * {@link vn.io.sontd.learning.server.constant.Constant#DAILY_VOCAB_TITLE_PREFIX}),
     * each with its study cards attached.
     *
     * @param limit the maximum number of study sets to return; non-positive values yield an empty list
     * @return up to {@code limit} study sets, ordered by creation time descending
     */
    List<StudySetDTO> findRecentlyCreated(int limit);

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
}
