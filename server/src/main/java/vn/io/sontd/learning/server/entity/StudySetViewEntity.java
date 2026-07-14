package vn.io.sontd.learning.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.io.sontd.learning.server.constant.TableField;

import java.time.LocalDateTime;

/**
 * Maps to the {@code study_set_views} table (see {@code db/create.sql}) — records
 * when a {@link UserEntity} last viewed a given {@link StudySetEntity}, one row
 * per user/study-set pair, so the "recently viewed" list can be built by
 * sorting on {@link #viewedAt}.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = TableField.TBL_STUDY_SET_VIEWS,
        uniqueConstraints = @UniqueConstraint(columnNames = {TableField.USER_ID, TableField.STUDY_SET_ID})
)
public class StudySetViewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = TableField.ID)
    private Long id;

    /** Viewing user's id (see {@code users.id}). */
    @Column(name = TableField.USER_ID)
    private Long userId;

    /** Viewed study set's id (see {@code study_sets.id}). */
    @Column(name = TableField.STUDY_SET_ID)
    private Long studySetId;

    /** Timestamp of the most recent view; updated (not just set on insert) every time the user views this study set. */
    @Column(name = TableField.VIEWED_AT)
    private LocalDateTime viewedAt;
}
