package vn.io.sontd.learning.server.request.studyset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request body for creating/updating a study set together with its cards.
 * If {@code id} is present the matching study set is updated (and its
 * existing cards fully replaced by {@code studyCards}); if {@code id} is
 * {@code null}, a new study set is created. {@code title} is not accepted
 * here: it's auto-generated from the study set's id (see
 * {@code StudySetServiceImpl.saveStudySet}).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetUpsertRequest {
    private Long id;
    private Long userId;
    private String description;
    private Boolean isPublic;
    private List<StudyCardUpsertRequest> studyCards;
}
