package vn.io.sontd.learning.server.request.studyset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request body for creating/updating a study set (and its cards) owned by the
 * current authenticated user. Unlike {@link StudySetUpsertRequest} (used by the
 * video-vocab admin flow, where {@code title}/{@code description}/{@code userId}
 * are always auto-generated), here they're supplied by the caller directly and
 * {@code userId} is taken from the current session, never from this request.
 * If {@code id} is present the matching study set is updated (and its existing
 * cards fully replaced by {@code studyCards}); if {@code id} is {@code null}, a
 * new study set is created.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyStudySetUpsertRequest {
    private Long id;
    private String title;
    private String description;
    private Boolean isPublic;
    private List<StudyCardUpsertRequest> studyCards;
}
