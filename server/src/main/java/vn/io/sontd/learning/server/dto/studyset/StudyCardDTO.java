package vn.io.sontd.learning.server.dto.studyset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Frontend-facing view of a {@link vn.io.sontd.learning.server.entity.StudyCardEntity},
 * nested under its owning {@link StudySetDTO}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyCardDTO {
    private Long id;
    private Long studySetId;
    private String term;
    private String definition;
    private String pronounceTerm;
    private String pronounceDef;
    private String imgUrl;
    private Integer wordType;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
