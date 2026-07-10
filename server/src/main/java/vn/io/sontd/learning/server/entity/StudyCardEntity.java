package vn.io.sontd.learning.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.io.sontd.learning.server.constant.TableField;

/**
 * Maps to the {@code study_cards} table (see {@code db/create.sql}) — a
 * single flashcard belonging to a {@link StudySetEntity}.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableField.TBL_STUDY_CARDS)
public class StudyCardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TableField.ID)
    private Long id;

    /** Owning study set's id (see {@code study_sets.id}). */
    @Column(name = TableField.STUDY_SET_ID)
    private Long studySetId;

    @Column(name = TableField.TERM)
    private String term;

    @Column(name = TableField.DEFINITION)
    private String definition;

    /** Phonetic/pronunciation hint for the term. */
    @Column(name = TableField.PRONOUNCE)
    private String pronounce;

    @Column(name = TableField.IMG_URL)
    private String imgUrl;

    /** Position of this card within its study set's card list. */
    @Column(name = TableField.DISPLAY_ORDER)
    private Integer displayOrder;
}
