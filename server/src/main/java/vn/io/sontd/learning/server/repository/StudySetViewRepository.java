package vn.io.sontd.learning.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.io.sontd.learning.server.entity.StudySetViewEntity;

import java.util.List;

/**
 * Data access for {@link StudySetViewEntity}.
 */
@Repository
public interface StudySetViewRepository extends JpaRepository<StudySetViewEntity, Long> {

    /**
     * Finds every study set the given user has viewed, most recently viewed first.
     *
     * @param userId the viewing user's id
     * @return the user's view records, ordered by {@code viewed_at} descending
     */
    List<StudySetViewEntity> findByUserIdOrderByViewedAtDesc(Long userId);
}
