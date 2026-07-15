package vn.io.sontd.learning.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.io.sontd.learning.server.entity.StudySetViewEntity;

import java.util.List;
import java.util.Optional;

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

    /**
     * Finds the view record for a given user/study-set pair, if one exists.
     *
     * @param userId     the viewing user's id
     * @param studySetId the viewed study set's id
     * @return the matching view record, or empty if this pair hasn't been recorded yet
     */
    Optional<StudySetViewEntity> findByUserIdAndStudySetId(Long userId, Long studySetId);
}
