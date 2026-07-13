package vn.io.sontd.learning.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.io.sontd.learning.server.entity.StudyCardEntity;

import java.util.Collection;
import java.util.List;

/**
 * Data access for {@link StudyCardEntity}.
 */
@Repository
public interface StudyCardRepository extends JpaRepository<StudyCardEntity, Long> {

    /**
     * Finds every study card belonging to any of the given study sets, sorted by display order.
     *
     * @param studySetIds the owning study sets' ids
     * @return matching study cards, ordered by display order ascending
     */
    List<StudyCardEntity> findByStudySetIdInOrderByDisplayOrderAsc(Collection<Long> studySetIds);

    /**
     * Deletes every study card belonging to the given study set.
     *
     * @param studySetId the owning study set's id
     */
    void deleteByStudySetId(Long studySetId);
}
