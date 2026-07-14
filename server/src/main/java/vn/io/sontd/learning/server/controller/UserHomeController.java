package vn.io.sontd.learning.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.io.sontd.learning.server.response.ResponseData;
import vn.io.sontd.learning.server.response.ResponseRoot;
import vn.io.sontd.learning.server.service.StudySetService;
import vn.io.sontd.learning.server.service.UserHomeService;

/**
 * User home screen endpoints.
 */
@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class UserHomeController extends BaseController {
    /** Default {@code limit} for {@link #getNewestStudySets} when the caller doesn't pass one. */
    private static final String DEFAULT_NEWEST_LIMIT = "6";

    private final UserHomeService userHomeService;
    private final StudySetService studySetService;

    /**
     * Returns the study sets the current user viewed most recently, most recent first.
     * This endpoint is public: an anonymous caller (no valid bearer token) gets an empty
     * list rather than a 401, so the home screen can render for logged-out visitors too.
     *
     * @param request the incoming request, whose bearer token (if any) identifies the user
     */
    @GetMapping("/recent-study-sets")
    public ResponseRoot getRecentStudySets(HttpServletRequest request) {
        return success(new ResponseData<>(userHomeService.getRecentlyViewedStudySets(request)));
    }

    /**
     * Returns the most recently created study sets, each with its study cards.
     *
     * @param limit the maximum number of study sets to return; defaults to {@value DEFAULT_NEWEST_LIMIT} when omitted
     */
    @GetMapping("/newest-study-sets")
    public ResponseRoot getNewestStudySets(@RequestParam(defaultValue = DEFAULT_NEWEST_LIMIT) int limit) {
        return success(new ResponseData<>(studySetService.findRecentlyCreated(limit)));
    }
}
