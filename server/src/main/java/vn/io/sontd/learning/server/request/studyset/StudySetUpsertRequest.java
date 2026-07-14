package vn.io.sontd.learning.server.request.studyset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request body for creating/updating a study set together with its cards.
 * If {@code id} is present the matching study set is updated (and its
 * existing cards fully replaced by {@code studyCards}); if {@code id} is
 * {@code null}, a new study set is created. {@code title}, {@code userId}
 * and {@code description} are not accepted here: they're always auto-generated
 * (see {@code StudySetServiceImpl.saveStudySet}).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetUpsertRequest {
    private Long id;
    private Boolean isPublic;
    private List<StudyCardUpsertRequest> studyCards;
}
