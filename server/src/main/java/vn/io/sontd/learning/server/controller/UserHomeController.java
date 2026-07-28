package vn.io.sontd.learning.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.io.sontd.learning.server.request.studyset.MyStudySetUpsertRequest;
import vn.io.sontd.learning.server.response.ResponseData;
import vn.io.sontd.learning.server.response.ResponseRoot;
import vn.io.sontd.learning.server.service.StudySetService;
import vn.io.sontd.learning.server.service.UserHomeService;
import vn.io.sontd.learning.server.utils.CommonUtils;

import java.util.List;

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
     */
    @GetMapping("/recent-study-sets")
    public ResponseRoot getRecentStudySets() {
        return success(new ResponseData<>(userHomeService.getRecentlyViewedStudySets()));
    }

    /**
     * Returns the study sets owned by the current user, each with its study cards, most
     * recently created first. This endpoint is public: an anonymous caller (no valid bearer
     * token) gets an empty list rather than a 401.
     */
    @GetMapping("/my-study-sets")
    public ResponseRoot getMyStudySets() {
        return success(new ResponseData<>(userHomeService.getMyStudySets()));
    }

    /**
     * Creates or updates a study set owned by the current authenticated user, together with
     * its cards, accepting the card images in the same request. Sent as
     * {@code multipart/form-data} with:
     * <ul>
     *   <li>{@code data} — the JSON {@link MyStudySetUpsertRequest};</li>
     *   <li>{@code files} — zero or more image files, referenced from each card by its
     *       {@code imageFileIndex} (0-based position in this list).</li>
     * </ul>
     * If {@code data.id} is present the matching study set is updated (only when it's owned by
     * the current user) and its existing cards are replaced by {@code data.studyCards}; if
     * {@code null}, a new study set owned by the current user is created.
     *
     * @param data  the JSON study set payload (a multipart part, so it's read as a raw string and parsed here)
     * @param files the uploaded card images, or {@code null} when no card has a new image
     */
    @PostMapping(value = "/my-study-sets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseRoot saveMyStudySet(@RequestPart("data") String data,
                                       @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        MyStudySetUpsertRequest request = CommonUtils.parseJson(data, MyStudySetUpsertRequest.class);
        return success(new ResponseData<>(studySetService.saveMyStudySet(request, files)));
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

    /**
     * Searches study sets by keyword: matches a study set's {@code title}/{@code description},
     * or any of its study cards' {@code term}/{@code definition} (case-insensitive, substring match).
     *
     * @param keyword the keyword to search for
     */
    @GetMapping("/search")
    public ResponseRoot search(@RequestParam String keyword) {
        return success(new ResponseData<>(studySetService.search(keyword)));
    }
}
