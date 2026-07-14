package vn.io.sontd.learning.server.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vn.io.sontd.learning.server.constant.Constant;
import vn.io.sontd.learning.server.controller.BaseController;
import vn.io.sontd.learning.server.request.studyset.StudySetUpsertRequest;
import vn.io.sontd.learning.server.response.ResponseData;
import vn.io.sontd.learning.server.response.ResponseRoot;
import vn.io.sontd.learning.server.service.StudySetService;
import vn.io.sontd.learning.server.utils.CommonUtils;

import java.util.List;

/**
 * Admin endpoints for managing video-vocabulary content.
 */
@RestController
@RequestMapping("/api/v1/admin/video-vocab")
@RequiredArgsConstructor
public class AdminVideoVocabController extends BaseController {
    private final StudySetService studySetService;

    /**
     * Returns every study set whose title contains {@link Constant#DAILY_VOCAB_TITLE_PREFIX}, each with its study cards.
     */
    @GetMapping("/daily-vocabulary-sets")
    public ResponseRoot getDailyVocabularySets() {
        return success(new ResponseData<>(studySetService.findByTitleContaining(Constant.DAILY_VOCAB_TITLE_PREFIX)));
    }

    /**
     * Creates or updates a study set together with its cards, accepting the card images
     * in the same request. Sent as {@code multipart/form-data} with:
     * <ul>
     *   <li>{@code data} — the JSON {@link StudySetUpsertRequest};</li>
     *   <li>{@code files} — zero or more image files, referenced from each card by its
     *       {@code imageFileIndex} (0-based position in this list).</li>
     * </ul>
     * If {@code data.id} is present the matching study set is updated and its existing cards
     * are replaced by {@code data.studyCards}; if {@code null}, a new study set is created.
     *
     * @param data  the JSON study set payload (a multipart part, so it's read as a raw string and parsed here)
     * @param files the uploaded card images, or {@code null} when no card has a new image
     */
    @PostMapping(value = "/study-sets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseRoot saveStudySet(@RequestPart("data") String data,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        StudySetUpsertRequest request = CommonUtils.parseJson(data, StudySetUpsertRequest.class);
        return success(new ResponseData<>(studySetService.saveStudySet(request, files)));
    }
}
