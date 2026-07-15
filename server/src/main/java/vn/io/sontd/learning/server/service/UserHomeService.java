package vn.io.sontd.learning.server.service;

import vn.io.sontd.learning.server.dto.studyset.StudySetDTO;

import java.util.List;

/**
 * Backing service for the user home screen.
 */
public interface UserHomeService {

    /**
     * Returns the study sets the current user viewed most recently, most recent first.
     * <p>
     * The endpoint is public (see {@link vn.io.sontd.learning.server.constant.Constant#INTERNAL_PERMIT_ALL}),
     * so this returns an empty list rather than failing when the caller is anonymous
     * (no token, an invalid/expired one, or a token for a user that no longer exists).
     *
     * @return the user's recently viewed study sets, or an empty list when not logged in
     */
    List<StudySetDTO> getRecentlyViewedStudySets();
}
