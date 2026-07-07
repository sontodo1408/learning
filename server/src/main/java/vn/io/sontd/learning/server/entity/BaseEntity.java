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

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(name = TableField.CREATED_AT, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = TableField.UPDATED_AT, updatable = false)
    private LocalDateTime updatedAt;
}
