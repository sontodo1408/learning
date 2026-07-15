package vn.io.sontd.learning.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * Finds the most recently created study sets owned by the given user id, most recent first.
     *
     * @param userId   the owning user's id (see {@code study_sets.user_id})
     * @param pageable carries the max result count (e.g. {@code PageRequest.of(0, limit)})
     * @return up to {@code pageable}'s page size matching study sets, ordered by creation time descending
     */
    List<StudySetEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Counts study sets whose title contains the given substring (case-sensitive).
     *
     * @param title the substring to search for within {@code study_sets.title}
     * @return the number of matching study sets
     */
    long countByTitleContaining(String title);

    /**
     * Finds every study set whose {@code title}/{@code description} contains the given keyword,
     * or that owns at least one study card whose {@code term}/{@code definition} contains it
     * (all case-insensitive).
     *
     * @param keyword the substring to search for
     * @return matching study sets, in no particular order
     */
    @Query("""
            SELECT ss FROM StudySetEntity ss
            WHERE LOWER(ss.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(ss.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR ss.id IN (
                   SELECT sc.studySetId FROM StudyCardEntity sc
                   WHERE LOWER(sc.term) LIKE LOWER(CONCAT('%', :keyword, '%'))
                      OR LOWER(sc.definition) LIKE LOWER(CONCAT('%', :keyword, '%'))
               )
            """)
    List<StudySetEntity> searchByKeyword(@Param("keyword") String keyword);
}
