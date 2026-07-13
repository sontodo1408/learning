package vn.io.sontd.learning.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.io.sontd.learning.server.constant.TableField;

/**
 * Maps to the {@code study_sets} table (see {@code db/create.sql}) — a
 * collection of {@link StudyCardEntity} flashcards owned by a {@link UserEntity}.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableField.TBL_STUDY_SETS)
public class StudySetEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TableField.ID)
    private Long id;

    /** Owning user's id (see {@code users.id}). */
    @Column(name = TableField.USER_ID)
    private Long userId;

    @Column(name = TableField.TITLE)
    private String title;

    @Column(name = TableField.DESCRIPTION)
    private String description;

    /** Whether other users can view this study set. */
    @Column(name = TableField.IS_PUBLIC)
    private Boolean isPublic;
}
