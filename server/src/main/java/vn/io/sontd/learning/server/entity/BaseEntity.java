package vn.io.sontd.learning.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.io.sontd.learning.server.constant.TableField;

import java.time.LocalDateTime;

/**
 * Common auditing columns shared by every entity.
 * Populated automatically by {@link AuditingEntityListener}, enabled via
 * {@code @EnableJpaAuditing} in {@link vn.io.sontd.learning.server.ServerApplication}.
 */
@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    /** Set automatically on insert. */
    @CreatedDate
    @Column(name = TableField.CREATED_AT, updatable = false)
    private LocalDateTime createdAt;

    /** Set automatically on insert (marked non-updatable, so it never changes afterward). */
    @LastModifiedDate
    @Column(name = TableField.UPDATED_AT, updatable = false)
    private LocalDateTime updatedAt;
}
