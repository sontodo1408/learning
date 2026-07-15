package vn.io.sontd.learning.server.service;

/**
 * Records which study sets a logged-in user has viewed.
 */
public interface StudySetViewService {

    /**
     * Records that the current user viewed the given study set: inserts a new
     * {@code study_set_views} row if this user/study-set pair has none yet, otherwise
     * refreshes the existing row's {@code viewed_at} to now.
     * A no-op if the caller is anonymous (no valid bearer token — views aren't tracked).
     *
     * @param studySetId the viewed study set's id
     */
    void recordView(Long studySetId);
}
