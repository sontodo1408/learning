package vn.io.sontd.learning.server.service;

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
     * Creates or updates a study set together with its cards.
     * If {@code request.getId()} is present, the matching study set is
     * updated and all of its existing cards are deleted and replaced by
     * {@code request.getStudyCards()}. If it's {@code null}, a new study set
     * (and all of its cards) is created.
     *
     * @param request the study set (and cards) to save
     * @return the saved study set, as a frontend-facing DTO
     */
    StudySetDTO saveStudySet(StudySetUpsertRequest request);
}
