package vn.io.sontd.learning.server.service;

import jakarta.servlet.http.HttpServletRequest;
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
     * so the caller is identified from the JWT bearer token on the request rather than
     * from the security context. If the request carries no token, an invalid/expired one,
     * or a token for a user that no longer exists, this returns an empty list instead of failing.
     *
     * @param request the incoming HTTP request, whose bearer token (if any) identifies the user
     * @return the user's recently viewed study sets, or an empty list when not logged in
     */
    List<StudySetDTO> getRecentlyViewedStudySets(HttpServletRequest request);
}
