package vn.io.sontd.learning.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.io.sontd.learning.server.entity.StudyCardEntity;

/**
 * Data access for {@link StudyCardEntity}.
 */
@Repository
public interface StudyCardRepository extends JpaRepository<StudyCardEntity, Long> {
}
