package vn.io.sontd.learning.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.io.sontd.learning.server.entity.StudySetEntity;

import java.util.List;

/**
 * Data access for {@link StudySetEntity}.
 */
@Repository
public interface StudySetRepository extends JpaRepository<StudySetEntity, Long> {

    /**
     * Finds every study set whose title contains the given substring (case-sensitive), sorted by title.
     *
     * @param title the substring to search for within {@code study_sets.title}
     * @return matching study sets, ordered by title ascending
     */
    List<StudySetEntity> findByTitleContainingOrderByTitleAsc(String title);

    /**
     * Finds the most recently created study sets whose title contains the given substring
     * (case-sensitive), most recent first.
     *
     * @param title    the substring to search for within {@code study_sets.title}
     * @param pageable carries the max result count (e.g. {@code PageRequest.of(0, limit)})
     * @return up to {@code pageable}'s page size matching study sets, ordered by creation time descending
     */
    List<StudySetEntity> findByTitleContainingOrderByCreatedAtDesc(String title, Pageable pageable);
}
