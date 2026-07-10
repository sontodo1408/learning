package vn.io.sontd.learning.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.io.sontd.learning.server.entity.StudySetEntity;

/**
 * Data access for {@link StudySetEntity}.
 */
@Repository
public interface StudySetRepository extends JpaRepository<StudySetEntity, Long> {
}
