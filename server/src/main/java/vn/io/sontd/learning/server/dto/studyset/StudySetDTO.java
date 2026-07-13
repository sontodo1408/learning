package vn.io.sontd.learning.server.dto.studyset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Frontend-facing view of a {@link vn.io.sontd.learning.server.entity.StudySetEntity},
 * including its {@link StudyCardDTO} cards.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudySetDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<StudyCardDTO> studyCards;
}
