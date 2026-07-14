package vn.io.sontd.learning.server.request.studyset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A single card within {@link StudySetUpsertRequest}. Cards have no id of
 * their own here: saving a study set always replaces its entire card list
 * (see {@code StudySetServiceImpl.saveStudySet}), so there is nothing to match
 * an existing card against.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyCardUpsertRequest {
    private String term;
    private String definition;
    private String pronounceTerm;
    private String pronounceDef;
    private String imgUrl;
    private Integer displayOrder;

    /**
     * 0-based index into the multipart {@code files} list identifying the newly
     * uploaded image for this card, or {@code null} when the card has no new upload.
     * When set, the server stores that file and overwrites {@link #imgUrl} with the
     * stored image's public URL; when {@code null}, {@link #imgUrl} is kept as-is
     * (an existing URL on update, or empty for a card without an image).
     */
    private Integer imageFileIndex;
}
